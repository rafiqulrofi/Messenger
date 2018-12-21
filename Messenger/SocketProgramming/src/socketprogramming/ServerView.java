package socketprogramming;

import java.net.*;
import java.io.*;
import java.util.*;

class WriteThread implements Runnable
{
	private Thread thr;
	private NetworkUtil nc;
	String name;
	public WriteThread(NetworkUtil nc,String name)
	{
		this.nc = nc;
		this.name=name;
		this.thr = new Thread(this);
                
                thr.start();

	}

	public void run()
	{
		try
		{
			//BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
			//Scanner input=new Scanner(System.in);

			while(true)
			{
				if((nc.sv.msg!=null) && nc.sv.flag==1){

                                    nc.write(name+":"+nc.sv.msg);
                                      System.out.println(nc.sv.msg);
                                      nc.sv.flag=0;
			        }
                                if((nc.cv.client_msg!=null) && nc.cv.client_flag==1){

                                    nc.write(name+":"+nc.cv.client_msg);
                                      System.out.println(nc.cv.client_msg);
                                      nc.cv.client_flag=0;
			        }
		}
                } catch (Exception e)
		{
			System.out.println (e);
		}
		nc.closeConnection();
	}
}




class ReadThread implements Runnable
{
	private Thread thr;
	private NetworkUtil nc;
        public String name=new String();

	public ReadThread(NetworkUtil nc,String s)
	{
            this.name=s;
		this.nc = nc;
		this.thr = new Thread(this);
		thr.start();
	}

	public void run()
	{
		try
		{
			while(true)
			{
				String t=nc.read().toString();
                                
				if(name.equals("Client"))
                                {
                                    nc.cv.jTextArea1.append(t+"\n");
                                }
                                if(name.equals("Server"))
                                {
                                    nc.sv.jTextArea1.append(t+"\n");
                                }

			}
		}catch(Exception e)
		{
			System.out.println (e);
		}
                nc.closeConnection();

	}
}




class NetworkUtil
{
	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	Object o;
        public ServerView sv=new ServerView();
        public ClientView cv=new ClientView();
        public String s=new String();
	public NetworkUtil(String s,int port,ClientView cv)
	{
            this.s=s;
            this.cv=cv;
		try
		{
                        
			this.socket=new Socket(s,port);
			oos=new ObjectOutputStream(socket.getOutputStream());
			ois=new ObjectInputStream(socket.getInputStream());
		}
		catch (Exception e)
		{
			System.out.println("In NetworkUtil : " + e.toString());
			//System.exit(0);
		}
	}

	public NetworkUtil(Socket s,ServerView sv)
	{
            this.sv=sv;
		try
		{
                        
			this.socket = s;
			oos=new ObjectOutputStream(socket.getOutputStream());
			ois=new ObjectInputStream(socket.getInputStream());
		}
		catch (Exception e)
		{
			System.out.println("In NetworkUtil : " + e.toString());
			//System.exit(0);

		}
	}

	public Object read()
	{
		try
		{	o=ois.readObject();
		}
		catch (Exception e)
		{
		  System.out.println("Reading Error in network : " + e.toString());
		  
		}
		return o;
	}

	public void write(Object o)
	{
		try
		{
			oos.writeObject(o);
		}
		catch (IOException e)
		{
			System.out.println("Writing  Error in network : " + e.toString());
			//System.exit(0);
		}
	}
	public void closeConnection() {
		try {

			ois.close();
			oos.close();
		}
		catch (Exception e) {
			System.out.println("Closing Error in network : "  + e.toString());
			//System.exit(0);
		}
	}

	public Socket getSocket()
	{
		return this.socket;
	}
}



class Server extends Thread
{
	private ServerSocket ServSock;
        public Thread t= new Thread();
        public ServerView sv=new ServerView();
	Server(ServerView sv)
	{
            this.sv=sv;
		try
		{
                    
                   t.start();
		}catch(Exception e)
		{
			System.out.println("in server"+e);
		}
	}
       public void run()
        {
           try
		{
                   ServSock = new ServerSocket(33333);
		//while (true)
		//{
			ServerThread m = new ServerThread(ServSock.accept(),sv);
		//}
		}catch(Exception e)
		{
			System.out.println("Server starts:"+e);
		}

        }

	//public static void main(String args[]) 	throws UnknownHostException, IOException
	//{
	//	Server objServer = new Server();
	//}
}

 class ServerThread implements Runnable
 {
	private Socket ClientSock;
	private Thread thr;
	private NetworkUtil nc;
        public ServerView sv=new ServerView();
	ServerThread(Socket client,ServerView sv)
	{
           this.sv=sv;
		this.ClientSock = client;
		this.thr = new Thread(this);
		thr.start();
	}

	public void run()
	{
		this.nc=new NetworkUtil(ClientSock,sv);
		new ReadThread(this.nc,"Server");
		new WriteThread(this.nc,"Server");

	}
}


public class ServerView extends javax.swing.JFrame {

    /** Creates new form ServerView */
    public ServerView() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jTextField1 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Server chat window");

        jButton1.setBackground(new java.awt.Color(0, 102, 102));
        jButton1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Start Server");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextArea1.setBackground(new java.awt.Color(102, 102, 102));
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jTextArea1.setForeground(new java.awt.Color(255, 255, 255));
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jTextField1.setBackground(new java.awt.Color(102, 102, 102));
        jTextField1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jTextField1.setForeground(new java.awt.Color(255, 255, 255));

        jButton2.setBackground(new java.awt.Color(0, 102, 102));
        jButton2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Send");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel2.setIcon(new javax.swing.ImageIcon("C:\\Users\\User\\Downloads\\black-background-wallpaper-600x375.jpg")); // NOI18N
        jLabel2.setText("jLabel2");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(jButton2))
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addGap(256, 256, 256)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    public String msg;
    public int flag=0;
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        
      
        this.msg = jTextField1.getText();
        this.jTextField1.setText("");
        this.flag=1;
          
        //System.out.println(msg);// TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[])throws UnknownHostException, IOException {
        
                ServerView sv = new ServerView();
        sv.setVisible(true);
            Server s=new Server(sv);
                s.start();
        }

    
/*
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
*/
     private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    public javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JTextArea jTextArea1;
    public javax.swing.JTextField jTextField1;
}
