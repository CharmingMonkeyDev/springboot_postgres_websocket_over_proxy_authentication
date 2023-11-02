package com.sunshineoxygen.inhome.repository.impl;

import com.sunshineoxygen.inhome.exception.BadUsageException;
import com.sunshineoxygen.inhome.exception.ExceptionType;
import com.sunshineoxygen.inhome.model.ListResponse;
import com.sunshineoxygen.inhome.model.SortField;
import com.sunshineoxygen.inhome.model.TMFDate;
import com.sunshineoxygen.inhome.repository.BaseRepository;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.util.MultiValueMap;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.io.Serializable;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

@NoRepositoryBean
public class BaseRepositoryImpl<T, ID extends Serializable> extends
        SimpleJpaRepository<T, ID> implements BaseRepository<T, ID> {

    private EntityManager entityManager;
    private Class<T> domainClass;
    private Class<T> entityClass;

    public BaseRepositoryImpl (JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager , Class<?> springDataRepositoryInterface) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    public BaseRepositoryImpl (JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    /**
     * Creates a new {@link SimpleJpaRepository} to manage objects of the given
     * domain type.
     *
     * @param domainClass
     * @param em
     */
    public BaseRepositoryImpl(Class<T> domainClass, EntityManager em) {
        this(JpaEntityInformationSupport.getEntityInformation(domainClass, em), em, null);
    }


    @Override
    public ListResponse<T> findByCriteria(MultiValueMap<String, String> queryParameters, Integer page, Integer size, SortField sortField, Class<T> entityClass)  throws BadUsageException {

        this.entityClass = entityClass;

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<T> cq = criteriaBuilder.createQuery( entityClass);
        CriteriaQuery<Long> cqCount = criteriaBuilder.createQuery(Long.class);

        List<Predicate> andPredicates = new ArrayList<>();
        List<Predicate> andPredicatesForCount = new ArrayList<>();

        Root<T> tt = cq.from( this.entityClass);
        Root<T> ttCount = cqCount.from( this.entityClass);

        andPredicates.addAll(getPredicates(criteriaBuilder, tt, queryParameters));
        andPredicatesForCount.addAll(getPredicates(criteriaBuilder, ttCount, queryParameters));

        cq.where(andPredicates.toArray(new Predicate[andPredicates.size()]));
        cq.select(tt);

        if(sortField != null){
            Order order = buildCriteriaOrder(tt, sortField.getField(),sortField.getDirection());
            cq.orderBy(order);
        }

        cqCount.select(criteriaBuilder.count(ttCount));
        cqCount.where(andPredicatesForCount.toArray(new Predicate[andPredicatesForCount.size()]));
        Long count = entityManager.createQuery(cqCount).getSingleResult();

        int viewPageSize = 0;

        if(queryParameters != null && queryParameters.size() > 1)
        {
            viewPageSize = new Integer(count.intValue() / size.intValue()) + 1;
            if(page > viewPageSize)
            {
                page = 1;
            }

        }

        TypedQuery<T> q = entityManager.createQuery(cq)
                .setFirstResult((page - 1) * size)
                .setMaxResults(size);

        List<T> resultList = q.getResultList();

        ListResponse<T> response = new ListResponse<>();
        response.setCount(count);
        response.setItems(resultList);
        response.setPage(page);

        return response;

    }

    private Order buildOrder(Path<T> path, String name,String direction) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        Order order = null;
        if (name != null) {
            Path<T> attribute = path.get(name);

            switch (direction) {
                case "ASC":
                    order = criteriaBuilder.asc(attribute);
                    break;
                case "DESC":
                    order = criteriaBuilder.desc(attribute);
                    break;
                default:
                    order = criteriaBuilder.asc(attribute);
                    break;
            }

        }
        return order;
    }

    private Order buildCriteriaOrder(Path tt, String name,String direction)  throws BadUsageException {
        Order order = null;
        int index = name.indexOf('.');

        Metamodel metamodel = entityManager.getMetamodel();
        EntityType<T> type2 = metamodel.entity(tt.getJavaType());

        if (index > 0 && index < name.length()) {
            // nested format : rootFieldName.subFieldName=value
            String rootFieldName = name.substring(0, index);
            String subFieldName = name.substring(index + 1);

            if(type2.getAttribute(rootFieldName).getPersistentAttributeType().equals(Attribute.PersistentAttributeType.MANY_TO_MANY) ||
                    type2.getAttribute(rootFieldName).getPersistentAttributeType().equals(Attribute.PersistentAttributeType.MANY_TO_ONE) ||
                    type2.getAttribute(rootFieldName).getPersistentAttributeType().equals(Attribute.PersistentAttributeType.ONE_TO_MANY) ||
                    type2.getAttribute(rootFieldName).getPersistentAttributeType().equals(Attribute.PersistentAttributeType.ONE_TO_ONE)) {

                From rootFr = (From) tt;
                From subFr = rootFr.join(rootFieldName);
                order = buildCriteriaOrder(subFr, subFieldName, direction);

            }  else {
                Path<T> root = tt.get(rootFieldName);
                order = buildCriteriaOrder(root, subFieldName, direction);
            }
        } else {
            order = buildOrder(tt,name,direction);
        }

        return order;
    }


    private List<Predicate> getPredicates(CriteriaBuilder criteriaBuilder, Root<T> tt, MultiValueMap<String, String> queryParameters)  throws BadUsageException {

        List<Predicate> andPredicates = new ArrayList<>();

        for (Map.Entry<String, List<String>> entry : queryParameters.entrySet()) {
            List<String> valueList = entry.getValue();
            Predicate predicate = null;
            if (valueList.size() > 1) {
                List<Predicate> orPredicates = new ArrayList<>();
                for (String currentValue : valueList) {
                    Predicate orPredicate = buildPredicate(tt, entry.getKey(), currentValue);
                    orPredicates.add(orPredicate);
                }
                predicate = criteriaBuilder.or(orPredicates.toArray(new Predicate[orPredicates.size()]));
            } else {
                predicate = buildPredicate(tt, entry.getKey(), valueList.get(0));
            }
            andPredicates.add(predicate);
        }

        return andPredicates;
    }

    private Predicate buildPredicate(Path tt, String name, String value)  throws BadUsageException {
        Predicate predicate = null;
        int index = name.indexOf('.');

        Metamodel metamodel = entityManager.getMetamodel();
        EntityType<T> type2 = metamodel.entity(tt.getJavaType());

        if (index > 0 && index < name.length()) {
            // nested format : rootFieldName.subFieldName=value
            String rootFieldName = name.substring(0, index);
            String subFieldName = name.substring(index + 1);

            if(type2.getAttribute(rootFieldName).getPersistentAttributeType().equals(Attribute.PersistentAttributeType.MANY_TO_MANY) ||
                    type2.getAttribute(rootFieldName).getPersistentAttributeType().equals(Attribute.PersistentAttributeType.MANY_TO_ONE) ||
                    type2.getAttribute(rootFieldName).getPersistentAttributeType().equals(Attribute.PersistentAttributeType.ONE_TO_MANY) ||
                    type2.getAttribute(rootFieldName).getPersistentAttributeType().equals(Attribute.PersistentAttributeType.ONE_TO_ONE)) {

                From rootFr = (From) tt;
                From subFr = rootFr.join(rootFieldName);
                predicate = buildPredicate(subFr, subFieldName, value);

            }  else {
                Path<T> root = tt.get(rootFieldName);
                predicate = buildPredicate(root, subFieldName, value);
            }
        } else {
            Path<T> path=null;
            // simple format : name=value
            boolean indexQ = name.contains(OperatorWithSign.EQ.getValue()) ||
                    name.contains(OperatorWithSign.GT.getValue()) ||
                    name.contains(OperatorWithSign.LT.getValue()) ||
                    name.contains(OperatorWithSign.GTE.getValue()) ||
                    name.contains(OperatorWithSign.LTE.getValue()) ||
                    name.contains(OperatorWithSign.EX.getValue()) ||
                    name.contains(OperatorWithSign.NE.getValue());
            if(indexQ){
                String [] array = null;
                if(name.contains(OperatorWithSign.EQ.getValue())){
                    array= name.split(OperatorWithSign.EQ.getValue());
                    name="eq";

                }else if(name.contains(OperatorWithSign.GT.getValue())){
                    array= name.split(OperatorWithSign.GT.getValue());
                    name = "gt";

                }else if(name.contains(OperatorWithSign.LT.getValue())){
                    array= name.split(OperatorWithSign.LT.getValue());
                    name = "lt";

                }else if(name.contains(OperatorWithSign.GTE.getValue())){
                    array= name.split(OperatorWithSign.GTE.getValue());
                    name = "gte";

                }else if(name.contains(OperatorWithSign.LTE.getValue())){
                    array= name.split(OperatorWithSign.LTE.getValue());
                    name = "lte";

                }else if(name.contains(OperatorWithSign.EX.getValue())){
                    array= name.split(OperatorWithSign.EX.getValue());
                    name = "ex";

                }else if(name.contains(OperatorWithSign.NE.getValue())){
                    array= name.split(OperatorWithSign.NE.getValue());
                    name = "ne";

                }
                value=array[1];
                path= tt.get(array[0]);

            }else{
                path = tt;
            }
            predicate = buildSimplePredicate(path, name, value);

        }
        return predicate;
    }

    private Predicate buildSimplePredicate(Path<T> path, String name, String value)  throws BadUsageException {
        Predicate predicate;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        if (isMultipleOrValue(value)) {
            // name=value1,value,...,valueN
            // => name=value1 OR name=value OR ... OR name=valueN
            List<String> valueList = convertMultipleOrValueToList(value);
            List<Predicate> orPredicates = new ArrayList<Predicate>();
            for (String currentValue : valueList) {
                Predicate orPredicate = buildPredicateWithOperator(path, name, currentValue);
                orPredicates.add(orPredicate);
            }
            predicate = criteriaBuilder.or(orPredicates.toArray(new Predicate[orPredicates.size()]));
        } else if (isMultipleAndValue(value)) {
            // name=(subname1=value1&subname2=value&...&subnameN=valueN)
            // => name.subname1=value1 AND name.subname2=value AND ... AND name.subnameN=valueN
            List<Map.Entry<String, String>> subFieldNameValue = convertMultipleAndValue(value);
            List<Predicate> andPredicates = new ArrayList<Predicate>();
            Path<T> root = path.get(name);
            for (Map.Entry<String, String> entry : subFieldNameValue) {
                String currentsubFieldName = entry.getKey();
                String currentValue = entry.getValue();
                Predicate andPredicate = buildPredicate(root, currentsubFieldName, currentValue);
                andPredicates.add(andPredicate);
            }
            predicate = criteriaBuilder.and(andPredicates.toArray(new Predicate[andPredicates.size()]));
        } else {
            predicate = buildPredicateWithOperator(path, name, value);
        }
        return predicate;
    }

    private static boolean isMultipleOrValue(String value) {
        return (value.indexOf(",") > -1);
    }

    private static boolean isMultipleAndValue(String value) {
        return (value.startsWith("(") && value.endsWith(")"));
    }

    private static List<String> convertMultipleOrValueToList(String value) {
        List<String> valueList = new ArrayList<String>();
        String[] tokenArray = value.split(",");
        valueList.addAll(Arrays.asList(tokenArray));
        return valueList;
    }

    private static List<Map.Entry<String, String>> convertMultipleAndValue(String multipleValue) {
        List<Map.Entry<String, String>> nameValueList = new ArrayList<Map.Entry<String, String>>();
        if (multipleValue.startsWith("(") && multipleValue.endsWith(")")) {
            String[] tokenArray = multipleValue.substring(1, multipleValue.length() - 1).split("&");
            for (String nameValue : tokenArray) {
                String[] split = nameValue.split("=");
                if (split.length == 2) {
                    String name = split[0];
                    String value = split[1];

                    nameValueList.add(new AbstractMap.SimpleEntry<String, String>(name, value));
                }
            }
        }
        return nameValueList;
    }

    // operators = and <> are compatibles with all types
    // operators < > <= >= are compatibles with numbers and dates
    // operator "LIKE" is compatible with String
    private boolean classCompatibleWithOperator(Class clazz, Operator operator) {
        if (operator == null) {
            return true;
        } else {
            switch (operator) {
                case NE:
                case EQ:
                    return true;
                case GT:
                    return true;
                case GTE:
                case LT:
                    return true;
                case LTE:
                    return (Date.class.isAssignableFrom(clazz)
                            || (clazz.isPrimitive() && !clazz.equals(boolean.class))
                            || Number.class.isAssignableFrom(clazz));
                case EX:
                    return String.class.equals(clazz);
                default:
                    return false;
            }
        }
    }

    protected Predicate buildPredicateWithOperator(Path<T> path, String name, String value) throws BadUsageException {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        BaseRepositoryImpl.Operator operator = BaseRepositoryImpl.Operator.fromString(name);

        if (operator == null) {
            Path<T> attribute = path.get(name);

            //Object valueObject = convertStringValueToObject(attribute, value);
            //return criteriaBuilder.equal(attribute, valueObject);
            if(attribute != null) {
                Class<? extends T> idAttribute  = attribute.getJavaType();
                return !idAttribute.equals(String.class) ?
                        criteriaBuilder.equal(attribute, convertStringValueToObject(attribute, value)) :
                        criteriaBuilder.like(criteriaBuilder.function("lower", String.class, (Expression) attribute), "%" + value.toLowerCase(new Locale("en","US")) + "%");
            }
            return criteriaBuilder.like(criteriaBuilder.function("lower", String.class, (Expression) attribute), "%" + value.toLowerCase(new Locale("en","US")) + "%");
        } else {
            Class javaType = path.getJavaType();
            if (!classCompatibleWithOperator(javaType, operator)) {
                throw new BadUsageException(ExceptionType.BAD_USAGE_OPERATOR, operator.getValue() + " operator incompatible with field");
            }
            Object valueObject = convertStringValueToObject(value, javaType);
            switch (operator) {
                case GT:
                    return criteriaBuilder.greaterThan((Expression) path, (Comparable) valueObject);
                case GTE:
                    return criteriaBuilder.greaterThanOrEqualTo((Expression) path, (Comparable) valueObject);
                case LT:
                    return criteriaBuilder.lessThan((Expression) path, (Comparable) valueObject);
                case LTE:
                    return criteriaBuilder.lessThanOrEqualTo((Expression) path, (Comparable) valueObject);
                case NE:
                    return criteriaBuilder.notEqual(path, valueObject);
                case EQ:
                    return criteriaBuilder.equal(path, valueObject);
                case EX:
                    return criteriaBuilder.like((Expression) path, value.replace('*', '%'));
                default: {
                    Path<T> attribute = path.get(name);
                    valueObject = convertStringValueToObject(value, attribute.getJavaType());
                    return criteriaBuilder.equal(attribute, valueObject);
                }
            }
        }

    }

    private Object convertStringValueToObject(Path<T> tt, String value) {
        Class javaType = tt.getJavaType();
        if (javaType.isEnum()) {
            Enum enumValue = safeEnumValueOf(javaType, value);
            return enumValue;
        }else if(javaType.getName().equals("java.util.UUID")){
            return UUID.fromString(value);
        } else {
            return value;
        }
    }

    private Object convertStringValueToObject(String value, Class clazz) throws BadUsageException {
        Object convertedValue = null;
        if (clazz.isEnum()) {
            convertedValue = safeEnumValueOf(clazz, value);
        } else if (Date.class.isAssignableFrom(clazz)) {
            convertedValue = TMFDate.parse(value);
        } else if ((clazz.isPrimitive() && !clazz.equals(boolean.class))
                || (Number.class.isAssignableFrom(clazz))) {
            try {
                convertedValue = NumberFormat.getInstance().parse(value);
            } catch (ParseException ex) {
                convertedValue = null;
            }
        } else {
            convertedValue = value;
        }
        if (convertedValue != null) {
            return convertedValue;
        } else {
            throw new BadUsageException(ExceptionType.BAD_USAGE_FORMAT, "Wrong format for value " + value);
        }

    }



    private Enum safeEnumValueOf(Class enumType, String name) {
        Enum enumValue = null;
        if (name != null) {
            try {
                enumValue = Enum.valueOf(enumType, name);
            } catch (Exception e) {
                enumValue = null;
            }
        }
        return enumValue;
    }

    enum Operator {

        EQ("eq"),
        GT("gt"),
        GTE("gte"),
        LT("lt"),
        LTE("lte"),
        NE("ne"),
        EX("ex");
        private String value;

        Operator(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

        public static BaseRepositoryImpl.Operator fromString(String value) {
            if (value != null) {
                for (BaseRepositoryImpl.Operator b : BaseRepositoryImpl.Operator.values()) {
                    if (value.equalsIgnoreCase(b.value)) {
                        return b;
                    }
                }
            }
            return null;
        }
    }

    enum OperatorWithSign {

        EQ("-eq-"),
        GT("-gt-"),
        GTE("-gte-"),
        LT("-lt-"),
        LTE("-lte-"),
        NE("-ne-"),
        EX("-ex-");
        private String value;

        OperatorWithSign(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

        public static BaseRepositoryImpl.OperatorWithSign fromString(String value) {
            if (value != null) {
                for (BaseRepositoryImpl.OperatorWithSign b : BaseRepositoryImpl.OperatorWithSign.values()) {
                    if (value.equalsIgnoreCase(b.value)) {
                        return b;
                    }
                }
            }
            return null;
        }
    }

}
