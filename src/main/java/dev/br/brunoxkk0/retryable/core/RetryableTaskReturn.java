package dev.br.brunoxkk0.retryable.core;

public record RetryableTaskReturn<T>(RetryableTask<T> task, T returnValue) {

}
