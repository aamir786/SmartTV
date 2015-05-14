import com.leapmotion.leap.*;
import com.leapmotion.leap.Gesture.State;
import java.io.IOException;
import java.lang.Math;
import java.awt.AWTException;
import java.awt.Robot;

class SmartTVListener extends Listener {
	public enum MyKeyBoard{
		LEFT_ARROW (37), UP_ARROW (38), RIGHT_ARROW (39), DOWN_ARROW (40);
		
		private int value;
		 
		private MyKeyBoard(int value) {
			this.value = value;
		}
	}
	
	static boolean bStartLeftSwipe = false;
	static boolean bStartRightSwipe = false;
	static boolean bStartDownSwipe = false;
	static boolean bStartUpSwipe = false;

	Robot robot = new Robot();
	
	public SmartTVListener() throws AWTException
	{
		robot.setAutoDelay(40);
		robot.setAutoWaitForIdle(true);
	}
	
	public void ArrowsKeys(MyKeyBoard k)
	{
//		for(int j = 0; j < 5; ++j)
		{
			robot.delay(40);
			robot.keyPress(k.value);
			robot.keyRelease(k.value);
		}
   }
	
    public void onInit(Controller controller) {
        System.out.println("Initialized");
    }

    public void onConnect(Controller controller) {
        System.out.println("Connected");
        controller.enableGesture(Gesture.Type.TYPE_SWIPE);
        controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
        controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
        controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);
    }

    public void onDisconnect(Controller controller) {
        //Note: not dispatched when running in a debugger.
        System.out.println("Disconnected");
    }

    public void onExit(Controller controller) {
        System.out.println("Exited");
    }

    public void onFrame(Controller controller) {
    	MyKeyBoard keyBoard;
    	
        // Get the most recent frame and report some basic information
        Frame frame = controller.frame();
/*        System.out.println("Frame id: " + frame.id()
                         + ", timestamp: " + frame.timestamp()
                         + ", hands: " + frame.hands().count()
                         + ", fingers: " + frame.fingers().count()
                         + ", tools: " + frame.tools().count()
                         + ", gestures " + frame.gestures().count());
*/
        //Get hands
        for(Hand hand : frame.hands()) {
            String handType = hand.isLeft() ? "Left hand" : "Right hand";
//            System.out.println("  " + handType + ", id: " + hand.id()
//                             + ", palm position: " + hand.palmPosition());

            // Get the hand's normal vector and direction
            Vector normal = hand.palmNormal();
            Vector direction = hand.direction();

            // Calculate the hand's pitch, roll, and yaw angles
/*            System.out.println("  pitch: " + Math.toDegrees(direction.pitch()) + " degrees, "
                             + "roll: " + Math.toDegrees(normal.roll()) + " degrees, "
                             + "yaw: " + Math.toDegrees(direction.yaw()) + " degrees");
*/
            // Get arm bone
            Arm arm = hand.arm();
/*            System.out.println("  Arm direction: " + arm.direction()
                             + ", wrist position: " + arm.wristPosition()
                             + ", elbow position: " + arm.elbowPosition());
*/
            // Get fingers
            for (Finger finger : hand.fingers()) {
/*                System.out.println("    " + finger.type() + ", id: " + finger.id()
                                 + ", length: " + finger.length()
                                 + "mm, width: " + finger.width() + "mm");
*/
                //Get Bones
                for(Bone.Type boneType : Bone.Type.values()) {
                    Bone bone = finger.bone(boneType);
/*                    System.out.println("      " + bone.type()
                                     + " bone, start: " + bone.prevJoint()
                                     + ", end: " + bone.nextJoint()
                                     + ", direction: " + bone.direction());
*/
                }
            }
        }

        // Get tools
        for(Tool tool : frame.tools()) {
/*            System.out.println("  Tool id: " + tool.id()
                             + ", position: " + tool.tipPosition()
                             + ", direction: " + tool.direction());
*/
        }

        GestureList gestures = frame.gestures();
        for (int i = 0; i < gestures.count(); i++) {
            Gesture gesture = gestures.get(i);

            switch (gesture.type()) {
                case TYPE_CIRCLE:
                    CircleGesture circle = new CircleGesture(gesture);

                    // Calculate clock direction using the angle between circle normal and pointable
                    String clockwiseness;
                    if (circle.pointable().direction().angleTo(circle.normal()) <= Math.PI/2) {
                        // Clockwise if angle is less than 90 degrees
                        clockwiseness = "clockwise";
                    } else {
                        clockwiseness = "counterclockwise";
                    }

                    // Calculate angle swept since last frame
                    double sweptAngle = 0;
                    if (circle.state() != State.STATE_START) {
                        CircleGesture previousUpdate = new CircleGesture(controller.frame(1).gesture(circle.id()));
                        sweptAngle = (circle.progress() - previousUpdate.progress()) * 2 * Math.PI;
                    }

/*                    System.out.println("  Circle id: " + circle.id()
                               + ", " + circle.state()
                               + ", progress: " + circle.progress()
                               + ", radius: " + circle.radius()
                               + ", angle: " + Math.toDegrees(sweptAngle)
                               + ", " + clockwiseness);
*/
                    break;
                case TYPE_SWIPE:
                    SwipeGesture swipe = new SwipeGesture(gesture);
                    
                    if (swipe.state() == State.STATE_START) {
                    	if(swipe.direction().getX() < -0.8f )		bStartLeftSwipe  = true;
                    	else if(swipe.direction().getX() > 0.8f )	bStartRightSwipe = true;
                    	if(swipe.direction().getY() < -0.8f )		bStartDownSwipe  = true;
                    	else if(swipe.direction().getY() > 0.8f )	bStartUpSwipe    = true;
                    	
                    	
                    }
                    
                    if (swipe.state() == State.STATE_STOP) {
                    	if(bStartLeftSwipe ==  true){
                    		ArrowsKeys(MyKeyBoard.LEFT_ARROW);
                    		bStartLeftSwipe = false;
                    	}
                    	if(bStartRightSwipe ==  true){
                    		ArrowsKeys(MyKeyBoard.RIGHT_ARROW);
                    		bStartRightSwipe = false;
                    	}
                    	if(bStartDownSwipe ==  true){
                    		ArrowsKeys(MyKeyBoard.DOWN_ARROW);
                    		bStartDownSwipe = false;
                    	}
                    	if(bStartUpSwipe ==  true){
                    		ArrowsKeys(MyKeyBoard.UP_ARROW);
                    		bStartUpSwipe = false;
                    	}
                    
                    
                    }
                    /*
                    if (swipe.state() == State.STATE_START) {
                    	if(swipe.direction().getX() < -0.8f )
                    		ArrowsKeys(MyKeyBoard.LEFT_ARROW);
                    	else if(swipe.direction().getX() > 0.8f )
                    		ArrowsKeys(MyKeyBoard.RIGHT_ARROW);
                    	
                    	if(swipe.direction().getY() < -0.8f )
                    		ArrowsKeys(MyKeyBoard.DOWN_ARROW);
                    	else if(swipe.direction().getY() > 0.8f )
                    		ArrowsKeys(MyKeyBoard.UP_ARROW);
                    }*/
                    System.out.println("  Swipe id: " + swipe.id()
                               + ", " + swipe.state()
                               + ", position: " + swipe.position()
                               + ", direction: " + swipe.direction()
                               + ", speed: " + swipe.speed());
                    break;
                case TYPE_SCREEN_TAP:
                    ScreenTapGesture screenTap = new ScreenTapGesture(gesture);
/*	                    System.out.println("  Screen Tap id: " + screenTap.id()
                               + ", " + screenTap.state()
                               + ", position: " + screenTap.position()
                               + ", direction: " + screenTap.direction());
*/
                    break;
                case TYPE_KEY_TAP:
                    KeyTapGesture keyTap = new KeyTapGesture(gesture);
/*                    System.out.println("  Key Tap id: " + keyTap.id()
                               + ", " + keyTap.state()
                               + ", position: " + keyTap.position()
                               + ", direction: " + keyTap.direction());
*/
                    break;
                default:
//                  System.out.println("Unknown gesture type.");
                    break;
            }
        }

        if (!frame.hands().isEmpty() || !gestures.isEmpty()) {
//            System.out.println();
        }
    }
}

public class LeapSmartTV {
	public static void main(String[] args) throws AWTException
	{	
		new LeapSmartTV();
		
        // Create a sample listener and controller
		SmartTVListener listener = new SmartTVListener();
        Controller controller = new Controller();

        // Have the sample listener receive events from the controller
        controller.addListener(listener);
        
     // here i set the app to run in background:
        controller.setPolicyFlags(Controller.PolicyFlag.POLICY_BACKGROUND_FRAMES);

        // Keep this process running until Enter is pressed
        System.out.println("Press Enter to quit...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Remove the sample listener when done
        controller.removeListener(listener);
    }
}
