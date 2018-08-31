package CSVReader;

/**
 * 读取大数据文件过程中，数据文件中部分数据在内存中的存贮空间。并对存贮给出了操作。
 * @author lenovo
 *
 */
public class ShareSpace {
	
	public final int MAX_SPACE = 1024;
	public int front;
	public int rear;
	private String[] space;
	public boolean dataOver;
	
	/**
	 * 构造函数。
	 */
	public ShareSpace(){
		
		front = 0;
		rear = 0;
		dataOver = false;
		space = new String[MAX_SPACE];
	}

	/**
	 * 以独占的方式将参数中的元素存贮到存贮空间中。
	 * @param item String的值，存贮到存贮空间中的元素
	 */
	public synchronized void inSpace(String item){
	
		rear = (rear + 1) % MAX_SPACE;
		space[rear] = item;	
	}
	
	/**
	 * 以独占的方式获取存贮空间中的第一个元素
	 * @return String类型的存贮空间中第一个元素
	 */
	public  synchronized String outSpace(){
		
		front = (front + 1) % MAX_SPACE;
		return space[front];		
	}
	
	/**
	 * 判断存贮空间中是否没有元素。
	 * 最大存储max_space-1个数据，
	 * @return true 如果存贮空间中没有元素，否则false
	 */
	public boolean isEmpty(){
		
		return front == rear;
	}
	
	/**
	 * 判断存贮单空间中是否已存贮满 数据
	 * @return true 如果存贮空间中没有闲置的存贮单元，否则为false
	 */
	public boolean isFull() {
		//System.out.println(front+"/"+rear);
		return ((rear + 1) % MAX_SPACE) == front;
	}
	
	/**
	 * 获取存贮队列中闲置存贮单元的个数
	 * @return 存贮空间中闲置存贮单元的个数
	 */
	public int getEmptyLength(){
		
		if (rear >= front)
			return MAX_SPACE - (rear - front) - 1;
		else
			return front - rear - 1;
	}
}
