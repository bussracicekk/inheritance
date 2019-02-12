/**
 * @author busra cicek
 */

//This program creates a output_files folder and saves all of output files to it.

package assignment2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map.Entry;



public class Main {
	
	static final String outputFileFolderName = "output_files";
	
	HashMap<String, Personnel> personnelMap = null;
	String pathOutputFile = null;

	
	public Main(String pathPersonelFile, String pathMonitoringFile) throws Exception
	{
		personnelMap = new HashMap<String, Personnel>();
		
		ReadPersonnelFile(pathPersonelFile);
		ReadMonitoringFile(pathMonitoringFile);
		//debugPersonnelList();  //(!)
	}
	
	
	public void execute() throws FileNotFoundException, UnsupportedEncodingException
	{
		for (Entry<String, Personnel> entry : personnelMap.entrySet()) {
			Personnel p = entry.getValue();			
			p.calculateTotalSalary();
			//debugOutput(p);
			writeOutput(p);
		}			
	}
	
	//(!) DEBUG...
//	public void debugOutput(Personnel p) throws FileNotFoundException, UnsupportedEncodingException
//	{
//		System.out.println("******************************************");  //(!)
//		System.out.println("Name: " + p.getNameSurname());
//		System.out.println("Registration Number: " + p.getRegistrationNumber());
//		System.out.println("Position: " + p.getPosition());
//		System.out.println("Year of Start: " + p.getStartYear());
//		System.out.println("Total Salary: " + p.getTotalSalary());	
//	}
	
	//(!) DEBUG...
//	public void debugPersonnelList()
//	{
//		for (Entry<String, Personnel> entry : personnelMap.entrySet()) {
//			String s = entry.getKey();
//			Personnel p = entry.getValue();
//			
//			System.out.println(s + " " + 
//							p.getNameSurname() + " " + 
//							p.getPosition() + " " + 
//							Integer.toString(p.getStartYear()) + " " +
//							Integer.toString(p.getWorkHoursPerWeek()[0]) + " " +
//							Integer.toString(p.getWorkHoursPerWeek()[1]) + " " +
//							Integer.toString(p.getWorkHoursPerWeek()[2]) + " " +
//							Integer.toString(p.getWorkHoursPerWeek()[3])
//					);			
//		}	
//	}		
	
	public void writeOutput(Personnel p) throws FileNotFoundException, UnsupportedEncodingException
	{
		File outputDir = new File(outputFileFolderName);
		if(!outputDir.exists()){
		    	outputDir.mkdir();
		}		
		
		String reg = p.getRegistrationNumber();
		
		PrintWriter writer = new PrintWriter(".\\" + outputFileFolderName + "\\" + reg + ".txt", "UTF-8");
		
		writer.println("Name: " + p.getNameSurname());
		writer.println("Registration Number: " + reg);
		writer.println("Position: " + p.getPosition());
		writer.println("Year of Start: " + p.getStartYear());
		writer.println("Total Salary: " + p.getTotalSalary());
		writer.close();		
	}
	
	
	public void ReadPersonnelFile(String file) throws Exception
	{
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			
		    String line;
		    while ((line = br.readLine()) != null) {
		    	
		    	// Splitting line into array...
		    	int i = 0;
		    	String[] personnelInfoArray = new String[5];  // [name and surname]  [registration number] [position of pesonnel] [and year of start] 
		        for(String value: line.split("\t")){
		        	personnelInfoArray[i++] = value.trim();
		        }				
					
				char personnelTypeIdentifier = personnelInfoArray[1].charAt(0);
				
				// Creating Personnel instances...
				if(personnelTypeIdentifier == 'M'){
					Personnel personnel = new Manager(personnelInfoArray[0].trim(), 
														personnelInfoArray[1].trim(), 
														personnelInfoArray[2].trim(), 
														Integer.parseInt(personnelInfoArray[3].trim()));
					
					personnelMap.put(personnelInfoArray[1].trim(), personnel);
				}
				else if(personnelTypeIdentifier == 'O'){
					Personnel personnel = new Officer(personnelInfoArray[0].trim(), 
														personnelInfoArray[1].trim(), 
														personnelInfoArray[2].trim(), 
														Integer.parseInt(personnelInfoArray[3].trim()));
					
					personnelMap.put(personnelInfoArray[1].trim(), personnel);
				}
				else if(personnelTypeIdentifier == 'S'){
					Personnel personnel = new Security(personnelInfoArray[0].trim(), 
														personnelInfoArray[1].trim(), 
														personnelInfoArray[2].trim(), 
														Integer.parseInt(personnelInfoArray[3].trim()));
					
					personnelMap.put(personnelInfoArray[1].trim(), personnel);
				}
				else if(personnelTypeIdentifier == 'W'){
					Personnel personnel = new WorkerFullTimeEmployee(personnelInfoArray[0].trim(), 
							personnelInfoArray[1].trim(), 
							personnelInfoArray[2].trim(), 
							Integer.parseInt(personnelInfoArray[3].trim()));					
					
					personnelMap.put(personnelInfoArray[1].trim(), personnel);			
				}
				else if(personnelTypeIdentifier == 'C'){
					Personnel personnel = new ChiefFullTimeEmployee(personnelInfoArray[0].trim(), 
							personnelInfoArray[1].trim(), 
							personnelInfoArray[2].trim(), 
							Integer.parseInt(personnelInfoArray[3].trim()));
					
					personnelMap.put(personnelInfoArray[1].trim(), personnel);				
				}
				else if(personnelTypeIdentifier == 'P'){
					Personnel personnel = new PartTimeEmployee(personnelInfoArray[0].trim(), 
							personnelInfoArray[1].trim(), 
							personnelInfoArray[2].trim(), 
							Integer.parseInt(personnelInfoArray[3].trim()));	
					
					personnelMap.put(personnelInfoArray[1].trim(), personnel);				
				}				
				else{
					throw new Exception("Unexpected personnel type!");
				}
		    }
		}		
	}
	

	public void ReadMonitoringFile(String file) throws FileNotFoundException, IOException
	{
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			
		    String line;
		    while ((line = br.readLine()) != null) {
		    	
		    	// Splitting line into array...
		    	int i = 0;
		    	String[] monitoringInfoArray = new String[5];  //[registration number] [1. week] [2. week] [3. week] [4. week]  
		        for(String value: line.split("\t")){
		        	monitoringInfoArray[i++] = value.trim();
		        }				
								
				String registrationNumber = monitoringInfoArray[0];
				
				// Find related personnel record...
				Personnel personnel = personnelMap.get(registrationNumber);
				if(personnel != null){				
					int[] workHoursPerWeek = new int[4]; 
					for (int j = 0; j < workHoursPerWeek.length; j++) {
						workHoursPerWeek[j] = Integer.parseInt(monitoringInfoArray[j+1]);
					}
					
					personnel.setWorkHoursPerWeek(workHoursPerWeek);
				}
		    }
		}		
	}	

	public abstract class Personnel{
		
		static final int standartWorkHoursPerWeek = 40;
		static final double defaultBaseSalary = 1800;

		private String nameSurname;
		private String registrationNumber;
		private String position;
		private int startYear;
		private int[] workHoursPerWeek; 
		private double totalSalary;
		
		
		public Personnel(String nameSurname, String registrationNumber, String position, int startYear)
		{
			this.nameSurname = nameSurname;
			this.registrationNumber = registrationNumber;
			this.position = position;
			this.startYear = startYear;
			workHoursPerWeek = new int[4];
		}
		
		
		public void calculateTotalSalary()
		{			
			double baseSalary = calculateBaseSalary();
			double ssBenefit = calculateSpecialSeviceBenefits();
			double severancePay = calculateSeverancePay();
			totalSalary = baseSalary + ssBenefit + severancePay;
		}
		
		
		public String getNameSurname() {
			return nameSurname;
		}
		public void setNameSurname(String nameSurname) {
			this.nameSurname = nameSurname;
		}
		public String getRegistrationNumber() {
			return registrationNumber;
		}
		public void setRegistrationNumber(String registrationNumber) {
			this.registrationNumber = registrationNumber;
		}
		public String getPosition() {
			return position;
		}
		public void setPosition(String position) {
			this.position = position;
		}
		public int getStartYear() {
			return startYear;
		}
		public void setStartYear(int startYear) {
			this.startYear = startYear;
		}
		public int[] getWorkHoursPerWeek() {
			return workHoursPerWeek;
		}
		public void setWorkHoursPerWeek(int[] workHoursPerWeek) {
			this.workHoursPerWeek = workHoursPerWeek;
		}
		public double getTotalSalary() {
			return totalSalary;
		}	

		// Abstract...
		public abstract double calculateBaseSalary();
		public abstract double calculateSpecialSeviceBenefits();
		public abstract double calculateSeverancePay();
	
	}
	
	
	/*
	Managers  have  a  base salary  (baseSalary), special service  benefits (ssBenefits)  and severance pay (severancePay).  
	Also,  they  have  overwork  salary  (overWorkSalary)  but  Managers  can  work maximum 8 hours per week and for each working 
	hour they are paid 5 TL. (if they work more than 8 hours per week, they will not be paid additional money). 
	
	
	
	Managers, Officers, Workers and Chiefs work for 40 standard hours per week, excluding the working hours to gain overwork salary.
	
	
	Base salary is 1800 TL and it is constant for Manager and Officer.
	
	Special service benefits is %135 of the base salary of Managers and %49 of the base salary of Officers.
	(!)Note:  ssBenefitRatio = 1.35
	 */
	public class Manager extends Personnel{
		
		static final int maxOverWorkHoursPerWeek = 8;
		static final double overWorkCostByHour = 5;
		static final double ssBenefitRatio = 1.35;
		
		public Manager(String nameSurname, String registrationNumber, String position, int startYear)
		{
			super(nameSurname, registrationNumber, position, startYear);
		}
		

		public double calculateBaseSalary()
		{
			int newWork = 0;			
			for (int i = 0; i < 4; i++) {
				int wOverWork = getWorkHoursPerWeek()[i] - standartWorkHoursPerWeek;
				if(wOverWork >= maxOverWorkHoursPerWeek ){
					newWork += maxOverWorkHoursPerWeek;
				}
				else {
					if(wOverWork < maxOverWorkHoursPerWeek){
						newWork += wOverWork;
					}
				}

			}

			return defaultBaseSalary +  newWork * overWorkCostByHour;
		}
		
		/*
		Special service benefits is %135 of the base salary of Managers and %49 of the base salary of Officers.	
		 */
		public double calculateSpecialSeviceBenefits()
		{	
			//return baseSalary * ssBenefitRatio; 
			return 1800 * ssBenefitRatio;
		}
		
		/*
		 SeverancePay : (current year - year of start) * 20 * 0,8 = X TL 
		 */
		public double calculateSeverancePay ()
		{
			int currentYear = Calendar.getInstance().get(Calendar.YEAR);
			return (currentYear - this.getStartYear()) * 20 * 0.8;
		}		
	}
	
	/*
	Officers  are  paid  the  same  way  with  Managers  for  base  salary  (baseSalary)  but  special  service 
	benefits  (ssBenefits),  severance  pay  (severancePay)  and  overwork  salary  (overWorkSalary)  are calculated in a 
	different way from Managers. Officers can work maximum 10 hours per week and for each working hour they are paid 4 TL.
	
	Managers, Officers, Workers and Chiefs work for 40 standard hours per week, excluding the working hours to gain overwork salary.
	
	Base salary is 1800 TL and it is constant for Manager and Officer.
	
	Special service benefits is %135 of the base salary of Managers and %49 of the base salary of Officers.
	(!)Not:  ssBenefitRatio = 0.49 
	 */
	public class Officer extends Personnel{
		
		static final int maxOverWorkHoursPerWeek = 10;
		static final double overWorkCostByHour = 4;	
		static final double ssBenefitRatio = 0.49;		
		
		public Officer(String nameSurname, String registrationNumber, String position, int startYear)
		{
			super(nameSurname, registrationNumber, position, startYear);
		}		
		
		public double calculateBaseSalary()
		{
			int newWork = 0;
			
			for (int i = 0; i < 4; i++) {
				
				int wOverWork = getWorkHoursPerWeek()[i] - standartWorkHoursPerWeek;
				if(wOverWork >= maxOverWorkHoursPerWeek ){
					
					newWork += maxOverWorkHoursPerWeek;
				}
				else{
					if(wOverWork < maxOverWorkHoursPerWeek){
						newWork += wOverWork;  
					}
				}
			}

			double p = defaultBaseSalary +  (newWork * overWorkCostByHour);
			return p;
		}
		
		public double calculateSpecialSeviceBenefits()
		{
			//return baseSalary * ssBenefitRatio;    		
			return 1800 * ssBenefitRatio;					
		}	
		
		public double calculateSeverancePay()
		{
			int currentYear = Calendar.getInstance().get(Calendar.YEAR);
			return (currentYear - this.getStartYear()) * 20 * 0.8;
		}	
	}
	
		
	
	/*
	For  Security,  salaries  are  calculated  based  on  the  number  of  working  hours  (hourOfWork)  and 
	severance pay (severancePay). In addition to working hours, they are paid 4 TL for transportation 
	(transMoney)  and  5  TL  food  (foodMoney)  per  day.  Security  can  work  maximum  9  hours  and 
	minimum 5 hours per day. They use permission (i.e. they do not work) one day of the week. For each 
	working hour they are paid 6,5 TL. 	 
	 */
	public class Security extends Personnel{
		
		//static final int maxWorkHoursPerDay = 9;
		//static final int minWorkHoursPerDay = 5;
		
		static final int workDaysOfWeek = 24; 
		static final double paidByHour = 6.5;
		static final int maxOverWorkHoursPerWeek = 54;
		static final int minOverWorkHoursPerWeek = 30;
		
		static final int foodMoneyPerDay = 5;
		static final int transportationMoneyPerDay = 4;	
		
		
		public Security(String nameSurname, String registrationNumber, String position, int startYear)
		{
			super(nameSurname, registrationNumber, position, startYear);
		}			
		
				
		public double calculateBaseSalary()
		{
			double p = 0;
			int newWork = 0;
			for (int i = 0; i < 4; i++) {
				int wHours = getWorkHoursPerWeek()[i];
				
				 
				if(wHours  >= minOverWorkHoursPerWeek ){
					
					if(wHours  < maxOverWorkHoursPerWeek ){
						newWork += wHours;
					}
				else{
					if(wHours >= maxOverWorkHoursPerWeek){
						newWork += maxOverWorkHoursPerWeek;
					}
				}
					
				}
			}
			p += newWork * paidByHour +(foodMoneyPerDay + transportationMoneyPerDay)* workDaysOfWeek;
			return p;
		}
		
		public double calculateSpecialSeviceBenefits()
		{
			return 0;  // Unavailable...
		}
		
		public double calculateSeverancePay()
		{
			int currentYear = Calendar.getInstance().get(Calendar.YEAR);
			double p = (currentYear - this.getStartYear()) * 20 * 0.8;
			return p;
		}
	}	


	
	public abstract class  Employee extends Personnel{  
		
		public Employee(String nameSurname, String registrationNumber, String position, int startYear)
		{
			super(nameSurname, registrationNumber, position, startYear);
		}		
	}

		
	
	/*
	The  salaries  of  Part-time  Employees  are  calculated  based  on  the  number  of  working  hours 
	(hourOfWork). Part-time Employees can work minimum 10 hours and maximum 20 hours a week and they are paid 12 TL per hour. 	 
	 */
	public class PartTimeEmployee extends Employee{
		
		static final int maxWorkHoursPerWeek = 20;
		static final int minWorkHoursPerWeek = 10;
		static final double paidByHour = 12;
		
		public PartTimeEmployee(String nameSurname, String registrationNumber, String position, int startYear)
		{
			super(nameSurname, registrationNumber, position, startYear);
		}		
		
		public double calculateBaseSalary()
		{
			double p = 0;	
			for (int i = 0; i < 4; i++) {

				int wHours = getWorkHoursPerWeek()[i];
				
				 
				if(wHours  > minWorkHoursPerWeek ){
					
					if(wHours  > maxWorkHoursPerWeek ){
						wHours = maxWorkHoursPerWeek;
					}
					p += wHours * paidByHour;
				}
			}

			return p;
		}
		
		public double calculateSpecialSeviceBenefits()
		{
			return 0;   // Unavailable...
		}
		
		public double calculateSeverancePay()
		{
			return 0;   // Unavailable...
		}			
	}	
	
	public abstract class FullTimeEmployee extends Personnel{
			
		static final int workDaysOfWeek = 20;    // Full-time Employees do not work at weekends...
		
		public FullTimeEmployee(String nameSurname, String registrationNumber, String position, int startYear)
		{
			super(nameSurname, registrationNumber, position, startYear);
		}	
	}
	
	
	/*
	The  salaries  of  Full-time  Employees  are  calculated  based  on  the  number  of  working  days 
	(dayOfWork)  and  severance  pay  (severancePay).  Full-time  Employees  do  not  work  at  weekends. 
	Workers  are  paid  73  TL  and  Chiefs  are  paid  84  TL  per  day.  Also,  they  have  overwork  salary 
	(overWorkSalary). Workers can work maximum 10 hours a week and are paid 4 TL per hour while 
	Chiefs can work maximum 8 hours a week and are paid 5 TL per hour to gain overwork salary. (if 
	they work more than their maximum hours, they will not be paid additional money). 
	 */	
	public class WorkerFullTimeEmployee extends FullTimeEmployee{
		
		static final int maxOverWorkHoursPerWeek = 10;
		static final int overWorkSalaryPerHour = 4;
		static final double paidByDay = 73;
		
		public WorkerFullTimeEmployee(String nameSurname, String registrationNumber, String position, int startYear)
		{
			super(nameSurname, registrationNumber, position, startYear);
		}

		public double calculateBaseSalary()
		{
			int newWork = 0;
			
			
			for (int i = 0; i < 4; i++) {

				int wOverWork = getWorkHoursPerWeek()[i] - standartWorkHoursPerWeek;
				if(wOverWork >= maxOverWorkHoursPerWeek ){
					newWork += maxOverWorkHoursPerWeek;
				}
				else if(wOverWork < maxOverWorkHoursPerWeek){
					newWork += wOverWork;
				}
				
			}
			
			return paidByDay * workDaysOfWeek +  newWork * overWorkSalaryPerHour;
		}
		
		public double calculateSpecialSeviceBenefits()
		{
			return 0; // Unavailable...
		}
		
		public double calculateSeverancePay()
		{
			int currentYear = Calendar.getInstance().get(Calendar.YEAR);
			return (currentYear - this.getStartYear()) * 20 * 0.8;
		}
	}


	/*
	The  salaries  of  Full-time  Employees  are  calculated  based  on  the  number  of  working  days 
	(dayOfWork)  and  severance  pay  (severancePay).  Full-time  Employees  do  not  work  at  weekends. 
	Workers  are  paid  73  TL  and  Chiefs  are  paid  84  TL  per  day.  Also,  they  have  overwork  salary 
	(overWorkSalary). Workers can work maximum 10 hours a week and are paid 4 TL per hour while 
	Chiefs can work maximum 8 hours a week and are paid 5 TL per hour to gain overwork salary. (if 
	they work more than their maximum hours, they will not be paid additional money). 
	 */		
	public class ChiefFullTimeEmployee extends FullTimeEmployee{
		
		static final int maxOverWorkHoursPerWeek = 8;
		static final int overWorkSalaryPerHour = 5;
		static final double paidByDay = 84;
		
		public ChiefFullTimeEmployee(String nameSurname, String registrationNumber, String position, int startYear)
		{
			super(nameSurname, registrationNumber, position, startYear);
		}

		public double calculateBaseSalary()
		{
			int newWork = 0;
			
			
			for (int i = 0; i < 4; i++) {

				int wOverWork = getWorkHoursPerWeek()[i] - standartWorkHoursPerWeek;
				if(wOverWork >= maxOverWorkHoursPerWeek ){
					newWork += maxOverWorkHoursPerWeek;
				}
				else if(wOverWork < maxOverWorkHoursPerWeek ){
					newWork += wOverWork;   
				}
				
			}

			return paidByDay * workDaysOfWeek +  newWork * overWorkSalaryPerHour;
		}
		
		public double calculateSpecialSeviceBenefits()
		{
			return 0;  // Unavailable...
		}
		
		public double calculateSeverancePay()
		{
			int currentYear = Calendar.getInstance().get(Calendar.YEAR);
			double p = (currentYear - this.getStartYear()) * 20 * 0.8;
			return p;
		}
	}	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		String path = null;
       
		String pathPersonelFile = args[0];
    	String pathMonitoringFile =args[1];
        
        try {
			Main payroll = new Main(pathPersonelFile, pathMonitoringFile);
			payroll.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

