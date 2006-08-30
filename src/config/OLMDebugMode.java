package config;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;
import java.util.Vector;

import toulmin.BeliefDesc;
import toulmin.Toulmin;
import toulmin.ToulminBacking;
import toulmin.ToulminClaim;
import toulmin.ToulminData;
import toulmin.ToulminList;
import toulmin.ToulminSubClaim;
import toulmin.ToulminWarrant;

public class OLMDebugMode
{
	static String diffs[]={"very_easy","easy","medium","difficult","very_difficult"};
	static String comps[]={"elementary","simple_conceptual","multi_step","complex"};

	static Random rand = new Random();

	static int index = 0;
	
	public static BeliefDesc getBeliefDesc()
	{
		BeliefDesc vec = new BeliefDesc();
        vec.add("deriv");
        vec.add("");
        vec.add("think");
        vec.add("");
        vec.add("");
        vec.add("");

		return vec;
	}
	
	public static Hashtable getBelief()
	{
		ToulminClaim claim = new ToulminClaim();
		//claim.setClaimSummary(0.2522037958866262);
		claim.setClaimSummary(rand.nextInt(100)/100D);
		ToulminData data = getData();
		ToulminList list = getList();
		
		Toulmin toul = new Toulmin();
		toul.setBeliefDesc(getBeliefDesc());
		toul.setClaim(claim);
		toul.setData(data);
		toul.setDepth(index);
		toul.setList(list);
		
		Hashtable tab = new Hashtable();
		tab.put(OLMQueryResult.CAT_TOULMIN, toul.toXMLRPC());
		return tab;
	}
	
	private static ToulminData getData()
	{
		ToulminData data = new ToulminData();
		ArrayList array = new ArrayList();
		{
			double cer[]={0, 0.45192931934954733, 0.0, 1, 0.4618113699281088, 0.08624065613784115, 2, 1.189185180615315E-5, 0.08624741887053795, 3, 0.0, 0.05187178441321505, 4, 0.0, 0.01928506755105313, 5, 0.49619889623723784, 0.051871784413214994, 6, 1.7376717792703867E-5, 0.08624193400455141, 7, 0.0, 0.05187178441321505, 8, 0.5287856130993998, 0.01928506755105308, 9, 1.8654584502924947E-5, 0.08624065613784118, 10, 0.5480706806504528, 0.0};
			for (int i=0;i<cer.length;i++)
				array.add(new Double(cer[i]));
		}
		data.setCertainty(array);
		//data.setDiscount(array);
		array = new ArrayList();
		{
			double cer[]={0, 0.45192931934954733, 1, 0.4618113699281088, 2, 1.189185180615315E-5, 3, 0.0, 4, 0.0, 5, 0.03437563445732289, 6, 5.484865986550716E-6, 7, 0.0, 8, 0.032581231996175374, 9, 1.277866710221082E-6, 10, 0.019283789684342908};
			for (int i=0;i<cer.length;i++)
				array.add(new Double(cer[i]));
		}
		data.setDistribution(array);
//		array = new ArrayList();
//		{
//			double cer[]={0.01509869171115925, 0.03019738342231848, 0.04046309510838288, 0.08092619021676573, 0.3428485813918287, 0.6856971627836577, 0.2678248649238495, 0.535649729847699, 0.27179559323872615, 0.5435911864774521, 0.4278973470423383, 0.8557946940846765, 0.347158565505995, 0.69431713101199, 0.34323130762527665, 0.6864626152505532, 0.2813938456561937, 0.5627876913123873, 0.28430667696684164, 0.5686133539336831, 0.2522037958866262, 0.504407591773252};
//			for (int i=0;i<cer.length;i++)
//				array.add(new Double(cer[i]));
//		}
//		data.setHistory(array);
		array = new ArrayList();
		{
			double cer[]={1, 0.6076628750806347, 2, 0.1458665653928953, 3, 0.1286668563124277, 4, 0.11780370321404263};
			for (int i=0;i<cer.length;i++)
				array.add(new Double(cer[i]));
		}
		data.setPignistic(array);

		data.uncertainty = 0.504407591773252;
		data.conflict = 0.45192931934954733;
		return data;
	}
	
	private static ToulminList getList()
	{
		ToulminList list = new ToulminList();

		index=0;
		Toulmin sub1 = getSub1();
		Toulmin sub2 = getSub3();
		Toulmin sub3 = getSub2();
		list.add(sub1);
		list.add(sub2);
		list.add(sub3);
		return list;
	}
	
	private static String getDiff()
	{
		int nb= rand.nextInt(diffs.length);
		return diffs[nb];
	}
	private static String getComp()
	{
		int nb= rand.nextInt(comps.length);
		return comps[nb];
	}
	
	private static int getPerf()
	{
		return rand.nextInt(50);
	}
	private static int getRelev()
	{
		return rand.nextInt(100);
	}
	
	private static Toulmin getSub1()
	{
		Toulmin sub1 = new Toulmin();
		ToulminSubClaim claim = new ToulminSubClaim();
		claim.setDimension("ATTRIBUTE.PERFORMANCE");
		claim.setValue("ATTRIBUTE.PERFORMANCE.80");
		
		ToulminList list = getSubList("ATTRIBUTE.PERFORMANCE.80");
		sub1.setClaim(claim);
		sub1.setDepth(list.size());
		sub1.setList(list);
		return sub1;
	}
	private static Toulmin getSub2()
	{
		Toulmin sub1 = new Toulmin();
		ToulminSubClaim claim = new ToulminSubClaim();
		claim.setDimension("ATTRIBUTE.PERFORMANCE");
		claim.setValue("ATTRIBUTE.OTHERS");
		
		ToulminList list = getSubList("ATTRIBUTE.OTHERS");
		sub1.setClaim(claim);
		sub1.setDepth(list.size());
		sub1.setList(list);
		return sub1;
	}
	private static Toulmin getSub3()
	{
		Toulmin sub1 = new Toulmin();
		ToulminSubClaim claim = new ToulminSubClaim();
		claim.setDimension("ATTRIBUTE.PERFORMANCE");
		claim.setValue("ATTRIBUTE.PERFORMANCE.40");
		
		ToulminList list = getSubList("ATTRIBUTE.PERFORMANCE.40");
		sub1.setClaim(claim);
		sub1.setDepth(list.size());
		sub1.setList(list);
		return sub1;
	}
	
	private static ToulminList getSubList(String attr)
	{
		ToulminList list = new ToulminList();
		
		int nb = 3+rand.nextInt(12);
		for (int i=0;i<nb;i++)
		{
			int relev = 0;
			ToulminWarrant war = new ToulminWarrant();
			ToulminBacking bac = OLMEventsConfig.ExerciseFinished();
			bac.addAttribute("ACTION",new Integer(8));
			bac.addAttribute("INDEX",new Integer(index));
			bac.addAttribute("DIRECT","1");
			if ("ATTRIBUTE.PERFORMANCE.40".equals(attr))
				bac.addAttribute("PERFORMANCE",new Double(getPerf()/100.));
			else if ("ATTRIBUTE.PERFORMANCE.80".equals(attr))
				bac.addAttribute("PERFORMANCE",new Double((50+getPerf())/100.));
			bac.addAttribute("TITLE","Derivative of a monomial");
			bac.addAttribute("TYPE","ExerciseFinished");
			bac.addAttribute("RELEVANCE",new Integer((relev=getRelev())));
			bac.addAttribute("COMPETLEVEL",getComp());
			bac.addAttribute("DIFFICULTY",getDiff());
			bac.addAttribute("ITEM","mbase://LeAM_calculus/exercisesDiffDeriv/fib_easyderiv");
			Vector vec = new Vector();
			vec.add(getBeliefDesc());
			vec.add(getBeliefDesc());
			vec.add(getBeliefDesc());
			bac.addAttribute(OLMQueryResult.EVIDENCE_FOCUS,vec);
			
			
			
			war.setData(getSubData());
			war.setIndex(index);
			war.setRelevance(relev);
			war.setBacking(bac);
			list.add(war);
			index++;
		}
		
//		war = new ToulminWarrant();
//		bac = OLMEventsConfig.ExerciseFinished();
//		bac.addAttribute("ACTION",new Integer(8));
//		bac.addAttribute("INDEX",new Integer(index));
//		bac.addAttribute("DIRECT","1");
//		bac.addAttribute("PERFORMANCE",new Double(getPerf()/100.));
//		bac.addAttribute("TITLE","Derivative of a monomial");
//		bac.addAttribute("TYPE","ExerciseFinished");
//		bac.addAttribute("RELEVANCE",new Integer(getPerf()));
//		bac.addAttribute("COMPETLEVEL",getComp());
//		bac.addAttribute("DIFFICULTY",getDiff());
//		bac.addAttribute("ITEM","mbase://LeAM_calculus/exercisesDiffDeriv/fib_easyderiv");
//		war.setData(getSubData());
//		war.setIndex(index);
//		war.setRelevance(52);
//		war.setBacking(bac);
//		list.add(war);
//		index++;
		return list;
	}
	
	private static ToulminData getSubData()
	{
		ToulminData data = new ToulminData();
		ArrayList array = new ArrayList();
		{
			double cer[]={0, 0.0, 0.0, 1, 0.0, 0.41111229050718745, 2, 0.0, 1.0, 3, 0.0, 1.0, 4, 0.0, 1.0, 5, 0.0, 1.0, 6, 0.0, 1.0, 7, 0.0, 1.0, 8, 0.0, 1.0, 9, 0.5888877094928125, 0.41111229050718745, 10, 1.0, 0.0};
			for (int i=0;i<cer.length;i++)
				array.add(new Double(cer[i]));
		}
		data.setCertainty(array);
		array = new ArrayList();
		{
			double cer[]={0, 0.0, 1, 0.0, 2, 0.0, 3, 0.0, 4, 0.0, 5, 0.0, 6, 0.0, 7, 0.0, 8, 0.0, 9, 0.5888877094928125, 10, 0.41111229050718745};
			for (int i=0;i<cer.length;i++)
				array.add(new Double(cer[i]));
		}
		data.setDistribution(array);
		array = new ArrayList();
		{
			double cer[]={0, 0.0, 1, 0.0, 2, 0.0, 3, 0.0, 4, 0.0, 5, 0.0, 6, 0.0, 7, 0.0, 8, 0.0, 9, 0.4769990446891781, 10, 0.5230009553108219};
			for (int i=0;i<cer.length;i++)
				array.add(new Double(cer[i]));
		}
		data.setDiscount(array);
		array = new ArrayList();
		{
			double cer[]={1, 0.10277807262679686, 2, 0.2990739757910677, 3, 0.2990739757910677, 4, 0.2990739757910677};
			for (int i=0;i<cer.length;i++)
				array.add(new Double(cer[i]));
		}
		data.setPignistic(array);
		return data;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Hashtable ss = getBelief();
		Toulmin rr = Toulmin.fromXMLRPC(ss);
		System.err.println(rr);
	}

}
