**Assigment-description:**

Create the Workers-class with functionality to the right [below in README].
Use condition variables.

The post()-methods should be
thread-safe (can be used freely in several
threads simultaneously).

The selection of programming language is
free, but not Python. Java, C++ or Rust
recommendations, but other programming languages
supporting condition variables also work
fine.

Add a stop method that ends the worker threads
for example when the task list is empty.

Add a post_timeout() method that runs the
task argument after a given number of
milliseconds.

**SKELETON** (Copied directly from PowerPoint):
```cpp
Workers worker_threads(4);

Workers event_loop(1);

worker_threads.start(); // Create 4 internal threads

event_loop.start(); // Create 1 internal thread

worker_threads.post([] {

  // Task A

});

worker_threads.post([] {

  // Task B
  // Might run in parallel with task A

});

event_loop.post([] {

  // Task C
  // Might run in parallel with task A and B

});

event_loop.post([] {

  // Task D

  // Will run after task C

  // Might run in parallel with task A and B

});

worker_threads.join(); // Calls join() on the worker threadsevent_loop.join(); // Calls join() on the event thread
```
