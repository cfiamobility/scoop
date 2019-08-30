package ca.gc.inspection.scoop.createpost;

/**
 * When the Presenter calls the Interactor to create a Post Request (using the static helper method
 * newPostRequest in CreatePostInteractor), the call is asynchronous.
 * This means that when the presenter's callback method is called, it won't know the values of variables
 * it may need.
 *
 * Abstract base class which can be extended to store and retrieve data for the Presenter's callback
 * using getters and setters. (i.e. EditCommentBundle)
 *
 * Why can't we just store the state of the variables in the Presenter and reference them in the
 * callback?
 * 1. Waiting for multiple callbacks which use the same variable names leads to race conditions which are
 *      difficult to test
 * 2. Parameter dependency injection increases modularity which makes testing easier
 * 3. Poor encapsulation leads to pollution of class field variable names
 *
 */
public abstract class InteractorBundle {

    public InteractorBundle() {

    }

}
