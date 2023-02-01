#ifndef WORKERS_H
#define WORKERS_H

#include <vector>
#include <thread>
#include <functional>
#include <list>
#include <condition_variable>
#include <mutex>
#include <atomic>
#include <string>
using namespace std; 

class Workers
{
	public: 
		Workers(int numThreads, string type);
		~Workers();
		void start();
		void post(function<void()> task);
		void join();
		void checkpoint();
		void post_timeout(function<void()> task, int ms);

	private: 
		vector<thread> threads;
		int numThreads = 0;
		string type;
		list<function<void()>> tasks;
		mutex task_mutex;
		condition_variable cv;
		atomic<bool> notified{ true };
		atomic<bool> run{ false };
		atomic<bool> sleeping{ false };
};

#endif 
