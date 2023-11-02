package com.sunshineoxygen.inhome.utils;


import com.sunshineoxygen.inhome.exception.ApplicationException;

import java.io.File;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SafeFile extends File {

	private static final long serialVersionUID = 1L;
	private static final Pattern PERCENTS_PAT = Pattern.compile("(%)([0-9a-fA-F])([0-9a-fA-F])");
	private static final Pattern FILE_BLACKLIST_PAT = Pattern.compile("([\\\\/:*?<>|])");
	private static final Pattern DIR_BLACKLIST_PAT = Pattern.compile("([*?<>|])");

	public SafeFile(String path) throws ApplicationException {
		super(path);
		try{
			doPathTraverseCheck(path);
			doDirCheck(this.getParent());
			doFileCheck(this.getName());
		}catch(Exception e) {
			e.printStackTrace();
			throw new ApplicationException("The request file path is not secure!");
		}
	}

	public SafeFile(String parent, String child) throws ApplicationException {
		super(parent, child);
		try{

			doPathTraverseCheck(parent);
			doPathTraverseCheck(child);

			doDirCheck(this.getParent());
			doFileCheck(this.getName());
		}catch(Exception e) {
			e.printStackTrace();
			throw new ApplicationException("The request file path is not secure!");
		}
	}

	public SafeFile(File parent, String child) throws ApplicationException {
		super(parent, child);
		try{

			doPathTraverseCheck(this.getParent());
			doPathTraverseCheck(child);
			doDirCheck(this.getParent());
			doFileCheck(this.getName());
		}catch(Exception e) {
			e.printStackTrace();
			throw new ApplicationException("The request file path is not secure!");
		}
	}

	public SafeFile(URI uri) throws ApplicationException {
		super(uri);
		try{

			doPathTraverseCheck(this.getParent());
			doPathTraverseCheck(this.getName());
			doDirCheck(this.getParent());
			doFileCheck(this.getName());
		}catch(Exception e) {
			e.printStackTrace();
			throw new ApplicationException("The request file path is not secure!");
		}
	}

	private void doPathTraverseCheck(String path) throws ApplicationException {
		if(path!=null && (path.contains("../") || path.contains("./")) ){
			throw new ApplicationException("Directory path (" + path + ") contains ../ or ./" );
		}
	}

	private void doDirCheck(String path) throws ApplicationException {
		Matcher m1 = DIR_BLACKLIST_PAT.matcher( path );
		if ( m1.find() ) {
			throw new ApplicationException("Directory path (" + path + ") contains illegal character: " + m1.group() );
		}

		Matcher m2 = PERCENTS_PAT.matcher( path );
		if ( m2.find() ) {
			throw new ApplicationException("Directory path (" + path + ") contains encoded characters: " + m2.group() );
		}

		if(path.contains("\\..") || path.contains("\\.")){
			throw new ApplicationException("Directory path (" + path + ") contains ../ or ./" );
		}

		int ch = containsUnprintableCharacters(path);
		if (ch != -1) {
			throw new ApplicationException( "Directory path (" + path + ") contains unprintable character: " + ch);
		}
	}

	private void doFileCheck(String path) throws ApplicationException {
		Matcher m1 = FILE_BLACKLIST_PAT.matcher( path );
		if ( m1.find() ) {
			throw new ApplicationException( "Directory path (" + path + ") contains illegal character: " + m1.group() );
		}

		Matcher m2 = PERCENTS_PAT.matcher( path );
		if ( m2.find() ) {
			throw new ApplicationException("File path (" + path + ") contains encoded characters: " + m2.group() );
		}

		int ch = containsUnprintableCharacters(path);
		if (ch != -1) {
			throw new ApplicationException( "File path (" + path + ") contains unprintable character: " + ch);
		}
	}

	private int containsUnprintableCharacters(String s) {
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			if (((int) ch) < 32 || ((int) ch) > 126) {
				return (int) ch;
			}
		}
		return -1;
	}

}
