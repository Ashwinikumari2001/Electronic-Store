package com.lcwd.electronic.store.ElectronicStore.exception;

public class ResourceNotFound extends RuntimeException{
    public ResourceNotFound(){
        super("Resource not found!!");
    }

    public ResourceNotFound(String message){
        super(message);
    }
}
