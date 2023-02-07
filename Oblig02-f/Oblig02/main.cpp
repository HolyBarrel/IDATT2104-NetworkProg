#include <iostream>
#include <mutex>
#include "Workers.h"
using namespace std; 

mutex print_safe;

void worker_greeting(thread::id id);

void event_greeting(thread::id id);

int main()
{
	Workers worker_threads(4, "Worker Loop");
	Workers event_loop(1, "Event Loop");

	cout << "\n Main-thread ID:\t" << this_thread::get_id() << endl;

	//Starting thread-loops
	worker_threads.start();
	event_loop.start();

	//Posting tasks
	worker_threads.post_timeout([] { worker_greeting(this_thread::get_id()); }, 1000);
	worker_threads.post([] { worker_greeting(this_thread::get_id()); });

	event_loop.post([] { event_greeting(this_thread::get_id()); });
	event_loop.post_timeout([] { event_greeting(this_thread::get_id()); }, 2000);


	//Joining thread-loops
	worker_threads.join(); 
	event_loop.join();

	return 0;
}

void worker_greeting(thread::id id)
{
	unique_lock<mutex> lock(print_safe);
	cout << endl << " WORKER greets from thread ID:" << "\t\t" << id << endl;
}

void event_greeting(thread::id id)
{
	unique_lock<mutex> lock(print_safe);
	cout << endl << " EVENT greets from thread ID:   " << id << endl;
}

