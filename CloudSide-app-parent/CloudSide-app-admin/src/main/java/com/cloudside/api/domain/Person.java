/**
 * @author Poci.
 * @date 2018/1/1
 */
package com.cloudside.api.domain;

import java.io.Serializable;
import java.util.List;

public class Person implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String name;
	
	private int age;
	
	private List<String> info;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public List<String> getInfo() {
		return info;
	}

	public void setInfo(List<String> info) {
		this.info = info;
	}
	
	@Override
    public String toString() {  
        return "Person [name=" + name + ", age=" + age + ", infos=" + info + "]";  
    }
	
}