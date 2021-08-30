package com.ruyuan.netty.chapter03.article21;

public class ChainPatternDemo {

	public static void main(String[] args) {
		// 业务流程1  依次执行功能1、功能2、功能3
		System.out.println("业务流程1.....");
		Handler thirdHandler = new Handler3(null);
		Handler secondHandler = new Handler2(thirdHandler);
		Handler firstHandler = new Handler1(secondHandler);
		firstHandler.execute();

		System.out.println("------- 分隔符 --------");

		// 业务流程2  依次执行功能2、功能1、功能3
		System.out.println("业务流程2.....");
		thirdHandler = new Handler3(null);
		secondHandler = new Handler1(thirdHandler);
		firstHandler = new Handler2(secondHandler);
		firstHandler.execute();
	}

	public static abstract class Handler {

		// 责任链中下一个执行器
		protected Handler nextHandler;

		public Handler(Handler nextHandler) {
			this.nextHandler = nextHandler;
		}

		// 执行器执行功能逻辑
		public abstract void execute();

	}

	// 功能1执行器
	public static class Handler1 extends Handler {
		public Handler1(Handler nextHandler) {
			super(nextHandler);
		}

		@Override
		public void execute() {
			System.out.println("执行功能1");
			// 下一个执行器不为空时才执行下一个功能
			if(nextHandler != null) {
				nextHandler.execute();
			}
		}
	}

	// 功能2执行器
	public static class Handler2 extends Handler {
		public Handler2(Handler nextHandler) {
			super(nextHandler);
		}

		@Override
		public void execute() {
			System.out.println("执行功能2");
			if(nextHandler != null) {
				nextHandler.execute();
			}
		}
	}

	// 功能3执行器
	public static class Handler3 extends Handler {
		public Handler3(Handler nextHandler) {
			super(nextHandler);
		}

		@Override
		public void execute() {
			System.out.println("执行功能3");
			if(nextHandler != null) {
				nextHandler.execute();
			}
		}
	}

}
