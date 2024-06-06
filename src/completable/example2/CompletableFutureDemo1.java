/*
 * CompletableFutureDemo1.java
 *
 * Copyright by Hien Ng
 * Da Nang

 */
package completable.example2;

import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 
 *
 * @author nhqhien
 * @version $Revision:  $
 */
public class CompletableFutureDemo1
{
    public static void main(String[] args) throws Throwable
    {
        List<String> strs = new ArrayList<>();
        strs.add("1");
        strs.add("3");
        strs.add("4");
        strs.add("5");
        strs.add("6");
        strs.add("7");
        
        ExecutorService executor = Executors.newFixedThreadPool(2);
        List<CompletableFuture<String>> collect = strs.stream().map(s ->
        {
            return CompletableFuture.supplyAsync( () -> {
                try
                {
                    return manipulate(s);
                }
                catch (ValidationException e)
                {
                    System.out.println(e.getMessage());
                    throw new CompletionException(e);
                }
            }, executor);

        }).collect(Collectors.toList());
        
        try
        {
            collect.stream().map(CompletableFuture::join).filter(Objects::nonNull).forEach(System.out::println);
        }
        catch (CompletionException e)
        {
            String mess = "ERRs";
            if (e.getCause() != null)
            {
                try
                {
                    throw e.getCause();
                }
                catch (ValidationException e1)
                {
                    throw e1;
                }
            }
            else
            {
                throw new Exception(mess, e);
            }
        }
        finally
        {
            if (!executor.isShutdown())
            {
                executor.shutdown();
            }
        }
        
        
        System.out.println("DONE");
    }


    private static String manipulate(String s) throws ValidationException
    {
        if (s.equals("1"))
        {
            throw new ValidationException("InValid Value");
        }
        return s;
    }
}



/*
 * Changes:
 * $Log: $
 */