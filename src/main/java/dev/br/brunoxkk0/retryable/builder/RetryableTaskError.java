package dev.br.brunoxkk0.retryable.builder;

import dev.br.brunoxkk0.retryable.core.RetryableTask;

public record RetryableTaskError(RetryableTask<?> task, Exception error) {

}
