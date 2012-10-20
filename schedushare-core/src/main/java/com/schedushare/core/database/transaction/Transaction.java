package com.schedushare.core.database.transaction;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Database transactions should be atomic meaning that if something goes wrong, the transaction 
 * should be rolled back. Rather than ensure atomicity in every transaction resulting in duplicate code,
 * we can use AOP (Aspect Oriented Programming). Guice enables this by providing a way of annotating methods so they are 
 * the target of requiring atomic behavior. 
 * 
 * For an overview of AOP refer to: http://en.wikipedia.org/wiki/Aspect-oriented_programming
 * 
 * TODO: test these annotations somehow.
 * 
 */
@Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
public @interface Transaction {
	//NO ACTION.
}