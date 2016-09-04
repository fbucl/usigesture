package gesture;

public class Dot {
	
	public boolean hole;
	public long time_millis, time_nano;
	public int x, y, button, orientation, angle, pressure;
	public String device;
	
	public Dot(int x, int y, boolean hole) {
		this(0, 0, "unknown", x, y, 0, 0, 0, 0);
		this.hole = hole;
	}
	
	public Dot(int x, int y) {
		this(x, y, false);
	}
	
	public Dot(long time_millis, long time_nano, String device, int x, int y, int button, int orientation, int angle, int pressure){
		this.hole = false;
		this.time_millis = time_millis;
		this.device = device;
		this.time_nano = time_nano;
		this.x = x;
		this.y = y;
		this.button = button;
		this.orientation = orientation;
		this.angle = angle;
		this.pressure = pressure;
	}
	
	public Dot(String wacomDataLine){
		if(wacomDataLine.equals("Not valid")){
			hole = true;
			time_millis = time_nano = x = y = button = orientation = angle = pressure = -1;
			device = "tablet";
		}
		else{
			String[] tabletDataLineSplitted = wacomDataLine.split(" ");
			hole = false;
			time_millis = Long.parseLong(tabletDataLineSplitted[0].split(":")[1]);
			time_nano = Long.parseLong(tabletDataLineSplitted[1].split(":")[1]);
			device = tabletDataLineSplitted[2];
			String[] pos = tabletDataLineSplitted[3].split(",");
			x = Integer.parseInt(pos[0].substring(1));
			y = Integer.parseInt(pos[1].substring(0,pos[1].length()-1));
			button = Integer.parseInt(tabletDataLineSplitted[4].split("=")[1]);
			orientation = Integer.parseInt(tabletDataLineSplitted[5].split("=")[1]);
			angle = Integer.parseInt(tabletDataLineSplitted[6].split("=")[1]);
			pressure = Integer.parseInt(tabletDataLineSplitted[7].split("=")[1]);	
		}
	}
	
	public String toString(){
		if(!hole)
			return "time_millis="+time_millis+" time_nano="+time_nano+" "+device+" pos=("+x+","+y+") button="+button+" orientation="+orientation+" angle="+angle+" pressure="+pressure;
		else
			return "Not valid";
	}

}
