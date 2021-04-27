package com.nihaoyin.ptsservice.test;

import java.util.EventListener;
import java.util.EventObject;
import java.util.Vector;

/**
 * 事件类。参数source起路由作用，判断事件来源，作用相当于JSP
 *  的servlet。此处定义了两个事件源，实际第二个没用到，只是想说明可以通过注入接口或超类的思想将事件类别作为判断依据。
 */
class EventClassOne extends EventObject {

    public EventClassOne(Object source) {
        /*选择执行哪中构造方式，此处只有EventObject(Object source)一种*/
        super(source);
    }
}
class EventClassTwo extends EventObject{
    public EventClassTwo(Object source) {
        super(source);
    }
}
/**事件源类。表明谁触发了事件，用于作为EventObject类的构造参数，在listener中作路由*/
class EventSource{
    private String who;
    Vector listeners=new Vector();
    public EventSource(String who){
        this.who=who;
    }
    public String getActioner(){
        return who;
    }
    public void addMyEventListener(MyEventListener listener){
        listeners.add(listener);
    }
    /*设定say方法能被MyEventListener对象监听到*/
    public void say(String words){
        System.out.println(this.getActioner()+"说："+words);
        for(int i=0;i<listeners.size();i++){
            MyEventListener listener=(MyEventListener) listeners.elementAt(i);
            /*发布事件。当然应该事先规划say方法事件能发布给哪些事件监听器。*/
            listener.onMyEvent(new EventClassOne(this));
        }
    }
}
/**
 * 自定义监听器中通过EventObject判断事件来源，所以前面说EventObject是起路由功能。
 */
class MyEventListener implements EventListener {
    /*EventListener是与EventObject同级的最原始的监听器，当然里面什么方法都没有*/
    public void onMyEvent(EventObject e){
        /*如果该类与EventObject实例处于同一个类中，可以直接使用==判断事件来源*/
        if(e.getSource() instanceof EventSource){
            /*事件来源于OtherSource时要处理的业务*/
            EventSource tempSrc=(EventSource)e.getSource();
            System.out.println("收到来自"+tempSrc.getActioner()+"的事件!");
            while(true){
                try{Thread.sleep(10000);}
                catch(Exception a){
                    a.printStackTrace();
                };
            }
        }
		/*else if(e.getSource() instanceof OtherSource){
			System.out.println("事件来源于OtherSource时要处理的业务");
		}*/
    }
}
/**
 * 关键点：new几个事件，在awt、swing等界面编程中，ActionEvent事件(即行为)
 * 事件是java已经实现的，如点击鼠标事件，所以组件A只需使用addXxxListener方法
 * 添加监听器即可，运行时用户点击组件A，则自动使用A产生一个相应事件，并执行所有
 * listener的actionPerformed方法处理事件业务。*/
public class Main{
    public static MyEventListener listener = null;
    public static void main(String[] args){

        listener = new MyEventListener();
        EventSource 小白 = new EventSource("小白");
        小白.addMyEventListener(listener);
        小白.say("今天天气不错");
        小白.say("适合出去走走");
    }
}
