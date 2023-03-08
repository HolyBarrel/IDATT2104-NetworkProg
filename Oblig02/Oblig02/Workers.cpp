#include "Workers.h"
#include <iostream>
#include <chrono>


Workers::Workers(int numThreads, string type)
{
    this->numThreads = numThreads;
    this->type = type;
}

Workers::~Workers()
{
    cout << endl << " Destroyed Worker-object of type:\t" << type << endl;
}

void Workers::start()
{
    for (int i = 0; i < numThreads; i++)
    {
        threads.emplace_back([&] {
            while (true)
            {
                function<void()> task;
                {
                    unique_lock<mutex> lock(task_mutex);
                    cv.wait(lock, [this] {return !tasks.empty() || exit; });

                    if (exit) return;

                    task = *tasks.begin();
                    tasks.pop_front();
       
                }
                task();
            }
            });
    }
}

void Workers::post(function<void()> task)
{
    {
        unique_lock<mutex> lock(task_mutex);
        tasks.emplace_back(task);
    }
    cv.notify_all();
}

void Workers::join()
{
    {
        unique_lock<mutex> lock(task_mutex);
        exit.exchange(true);
    }
    cv.notify_all();
    for (auto& t : threads)
    {
        t.join();
    }
    threads.clear();
}

void Workers::post_timeout(function<void()> task, int ms)
{
    this_thread::sleep_for(chrono::milliseconds(ms)); 
    this->post(task);   
}
