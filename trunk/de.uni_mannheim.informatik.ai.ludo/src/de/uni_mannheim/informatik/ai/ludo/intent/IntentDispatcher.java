/*
* Copyright (C) 2010 Gregor Trefs, Dominique Ritze
* 
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package de.uni_mannheim.informatik.ai.ludo.intent;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The IntentDispatcher asynchronously executes the given intents.
 * It corresponds to the executor of the command pattern.
 * @author gtrefs
 */
public class IntentDispatcher {

    // Singleton
    private static IntentDispatcher INSTANCE = new IntentDispatcher();

    private Thread thread;

    // The queue holding the intients (FIFO)
    private Queue<Intent> queue;

    private IntentDispatcher(){
        queue = new ConcurrentLinkedQueue<Intent>();
    }

    /**
     * Start the dispatcher.
     */
    public void start(){
        if(thread != null){
            return;
        }
        thread = new Thread(new QueueRunner());
        thread.start();
    }

    /**
     * Stop the dispatcher.
     */
    public void stop(){
        if(thread != null)
            thread = null;
    }

    public static IntentDispatcher getInstance(){
        return INSTANCE;
    }

    /**
     * Puts an intent on the queue.
     * The dispatching works in FIFO order.
     * @param intent Intent to be excecuted by the dispatcher
     */
    public void dispatchIntent(Intent intent){
        queue.offer(intent);
    }

    private class QueueRunner implements Runnable{
        @Override
        public void run() {
            while(thread != null){
                Intent head = queue.poll();
                if(head==null){
                    continue;
                }
                head.execute();
            }
        }
    }
}