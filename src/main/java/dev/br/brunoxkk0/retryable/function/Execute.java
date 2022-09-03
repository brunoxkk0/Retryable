package dev.br.brunoxkk0.retryable.function;

import dev.br.brunoxkk0.retryable.core.RetryableTask;

@FunctionalInterface
public interface Execute<T>{
    T execute(RetryableTask<T> task) throws Exception;
}
