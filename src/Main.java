import java.lang.ref.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    //启动jvm参数：-Xmx128m -Xms128m
    public static void main(String[] args) {
//        System.out.println("Hello World!");
        int size = 1024;

        //出现oom错误
        //Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
//        List<byte[]> bytesList = new ArrayList<>(size);
//        for (int i = 0; i < size; i++) {
//            //申请1M大小的数组
//            bytesList.add(new byte[1024 * 1024]);
//        }


        //没有异常，软引用
//        List<Reference> bytesList = new ArrayList<>(size);
//        for (int i = 0; i < size; i++) {
//            byte[] b = new byte[1024 * 1024];
////            Reference<byte[]> rf = new SoftReference(b);
//            Reference<byte[]> rf = new WeakReference(b);
//            bytesList.add(rf);
//        }

        List<Reference> bytesList = new ArrayList<>(size);
        ReferenceQueue rq = new ReferenceQueue();
        Thread t = new Thread(()->{
            while(true){
                if(rq.poll() != null) {
                    try {
                        Reference reference = rq.remove(100);
                        if (reference != null) {
                            System.out.println(reference.toString());
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();

        for (int i = 0; i < size; i++) {

            byte[] b = new byte[1024 * 1024];
            Reference<byte[]> rf = new PhantomReference<>(b,rq);
            bytesList.add(rf);
//            b = null;

        }
        t.interrupt();

    }
}
