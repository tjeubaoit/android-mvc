package io.tjeubaoit.android.mvc.message;

import io.tjeubaoit.android.mvc.async.AsyncResult;
import io.tjeubaoit.android.mvc.async.Future;
import io.tjeubaoit.android.mvc.async.Handler;

/**
 * TODO: Class description here.
 *
 * @author <a href="https://github.com/tjeubaoit">tjeubaoit</a>
 */
class MessageImpl<R> implements Message {

    private String action;
    private Object body;
    private Handler<AsyncResult<R>> replyHandler;
    private android.os.Handler androidHandler;

    public MessageImpl(String action, Object body, android.os.Handler osHandler,
                       Handler<AsyncResult<R>> replyHandler) {
        this.action = action;
        this.body = body;
        this.replyHandler = replyHandler;
        this.androidHandler = osHandler;
    }

    @Override
    public String action() {
        return action;
    }

    @Override
    public Object body() {
        return body;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void reply(final Object response) {
        if (replyHandler != null) {
            androidHandler.post(new Runnable() {
                @Override
                public void run() {
                    replyHandler.handle((Future.Factory.succeededFuture((R) response)));
                }
            });
        }
    }

    @Override
    public void fail(final String failureMessage) {
        if (replyHandler != null) {
            androidHandler.post(new Runnable() {
                @Override
                public void run() {
                    replyHandler.handle(Future.Factory.<R>failedFuture(failureMessage));
                }
            });
        }
    }

    @Override
    public void fail(final Throwable throwable) {
        if (replyHandler != null) {
            androidHandler.post(new Runnable() {
                @Override
                public void run() {
                    replyHandler.handle(Future.Factory.<R>failedFuture(throwable));
                }
            });
        }
    }
}