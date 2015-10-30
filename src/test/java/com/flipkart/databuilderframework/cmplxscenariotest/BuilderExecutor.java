package com.flipkart.databuilderframework.cmplxscenariotest;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

//wrapper to measure context switch time
public class BuilderExecutor implements ExecutorService {

	private final  ExecutorService exec;
	private volatile int rejectionCounter = 0;

	public BuilderExecutor(int size, int queueSize) {
		this.exec = new ThreadPoolExecutor(size, size, 0, TimeUnit.SECONDS, 
				new ArrayBlockingQueue<Runnable>(100),
				new ThreadFactory() {
			int i=0;
			@Override
			public Thread newThread(Runnable r) {
				Thread th = new Thread(r,"builder-"+(i++));
				return th;
			}
		}, new RejectedExecutionHandler() {

			@Override
			public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
				rejectionCounter++; // not the safest but will give some idea
			}
		});
	}

	@Override
	public void execute(Runnable command) {
		final Long start = System.currentTimeMillis();
		final Runnable runnableRef = command;
		exec.execute(new Runnable() {
			@Override
			public void run() {
				long duration = System.currentTimeMillis()-start;
				if(duration > 250){
					System.out.println("context switch time "+duration);
				}
				runnableRef.run();
			}
		});
	}

	@Override
	public void shutdown() {
		exec.shutdown();

	}

	@Override
	public List<Runnable> shutdownNow() {
		return exec.shutdownNow();
	}

	@Override
	public boolean isShutdown() {
		return exec.isShutdown();
	}

	@Override
	public boolean isTerminated() {
		return exec.isTerminated();
	}

	@Override
	public boolean awaitTermination(long timeout, TimeUnit unit)
			throws InterruptedException {
		return exec.awaitTermination(timeout, unit);
	}

	@Override
	public <T> Future<T> submit(Callable<T> task) {
		final Long start = System.currentTimeMillis();
		final Callable<T> taskref = task;
		return exec.submit(new Callable<T>() {
			@Override
			public T call() throws Exception {
				System.out.println("context switch time "+(System.currentTimeMillis()-start));
				return taskref.call();
			}
		});
	}

	@Override
	public <T> Future<T> submit(Runnable task, T result) {
		return null;
	}

	@Override
	public Future<?> submit(Runnable task) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks)
			throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<Future<T>> invokeAll(
			Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
					throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks)
			throws InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks,
			long timeout, TimeUnit unit) throws InterruptedException,
			ExecutionException, TimeoutException {
		// TODO Auto-generated method stub
		return null;
	}

}
