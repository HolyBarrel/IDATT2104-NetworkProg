#include "Workers.h"
#include <iostream>
#include <chrono>
//#include <sstream>


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
    run = true;
    for (int i = 0; i < numThreads; i++)
    {
        threads.emplace_back([&] {
            while (run)
            {
                function<void()> task;
                {
                    unique_lock<mutex> lock(task_mutex);
                    while (!notified) cv.wait(lock);
                    if (!tasks.empty())
                    {
                        task = *tasks.begin();
                        tasks.pop_front();
                    }
       
                }
                if (task) {
                    task();
                    notified = true;
                    checkpoint();
                }
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
    for (auto& t : threads)
    {
        /*stringstream ss; //Coul be inplemented, but needs further locking to display correctly.
        ss << t.get_id();
        string print = "\n\t\t\t\t\t\tJoin on thread ID: " + ss.str() + "\n";
        cout << print;*/
        t.join();
    }
}

void Workers::checkpoint()
{
    if(tasks.empty() && !sleeping) run = false;
}

void Workers::post_timeout(function<void()> task, int ms)
{
    sleeping.exchange(true);
    this_thread::sleep_for(chrono::milliseconds(ms)); 
    sleeping.exchange(false);
    this->post(task);   
}
