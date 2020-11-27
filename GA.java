//package GA;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GA {
 static int geneLength = 22;
 static int individualSize = 20;//种群大小
 static int maxEvolve = 10000;//最大迭代次数
 static double crossoverProbability = 0.7;//基因重组概率
 static double mutationProbablility = 0.2;//基因突变概率
 static double sumFit = 0.0;
 static class individual implements Cloneable{
 double fit;
 double Pl;
 double Pr;
 int[] gene;

 @Override
 public String toString() {
 return "individual [fit=" + fit + ", Pl=" + Pl + ", Pr=" + Pr + ", gene=" + Arrays.toString(gene)
 + "]";
 }

 public individual(double fit, double pl, double pr, int[] gene) {
 super();
 this.fit = fit;
 Pl = pl;
 Pr = pr;
 this.gene = gene;
 }

 public Object clone(){
 individual idd = null;
 try {
 idd = (individual) super.clone();
 } catch (CloneNotSupportedException e) {
 // TODO Auto-generated catch block
 e.printStackTrace();
 }
 idd.gene = this.gene.clone();
 return idd;
 }
 }
 
 static List<individual> initPop(){
 List<individual> population = new ArrayList<individual>();
 int ca = 0;
 while(ca < individualSize){
 int[] gene = new int[geneLength];
 for(int i=0; i<geneLength; i++){
 gene[i] = (int)(Math.random()*100)%2;
 }
 population.add(new individual(0.0, 0.0, 0.0, gene));
 ca++;
 }
 return population;
 } 
 
 static void updatefit(List<individual> population){
 int constant = 4194103;
 for(int i=0; i<individualSize; i++){
 StringBuffer sb = new  StringBuffer();
 for(int j=0; j<geneLength; j++){
 sb.append(population.get(i).gene[j]);
 }
 int temp = Integer.valueOf(sb.toString(), 2);
 double x = -1.0 + 3.0*(temp*1.0/constant*1.0);
 population.get(i).fit = x*Math.sin(10*Math.PI*x)+2.0;
 sumFit += population.get(i).fit;
 }
 }


//筛选函数最大值
 static individual bestIndividual(List<individual> population){
 individual best = (individual) population.get(0).clone();
 for(int i=1; i<individualSize; i++){
 if(population.get(i).fit > best.fit){
 best = (individual) population.get(i).clone();
 }
 }
 return best;
 }


//筛选函数最小值
 static individual bestIndividual0(List<individual> population){
 individual best = (individual) population.get(0).clone();
 for(int i=1; i<individualSize; i++){
 if(population.get(i).fit < best.fit){
 best = (individual) population.get(i).clone();
 }
 }
 return best;
 }
 
 //种群选择
 static List<individual> selectPop(List<individual> population){
 int ca = 0;
 List<individual> newPopulation = new ArrayList<individual>();
 double start = 0.0;
 for(int i=0; i<individualSize; i++){
 double p = population.get(i).fit/sumFit;
 population.get(i).Pl = start;
 population.get(i).Pr = p + start;
 start = population.get(i).Pr;
 
 }
 while(ca < individualSize){
 boolean flag = false;
 double P = Math.random();
 for(int j=0; j<individualSize; j++){
 if(P>population.get(j).Pl && P<population.get(j).Pr){
 newPopulation.add(population.get(j));
 flag = true;
 break;
 }
 }
 if(!flag){
 newPopulation.add(bestIndividual(population));
 }
 ca++;
 }
 return newPopulation;
 }
 
 static void crossover(List<individual> population){
 boolean flag = false;
 int father = 0;
 int mother = 0;
 
 for(int i=0; i<individualSize; i++){
 double P = Math.random();
 if(P < crossoverProbability){
 if(flag){
 mother = i;
 int l = (int) (Math.random()*geneLength);
 int r = (int) (Math.random()*geneLength);
 if(l>r){
 int t = l;
 l = r;
 r = t;
 }
 int t[] = new int[geneLength];
 for(int f=l; f<r; f++){
 t[f] = population.get(father).gene[f];
 population.get(father).gene[f] = population.get(mother).gene[f];
 population.get(mother).gene[f] = t[f];
 }
 flag = false;
 }
 else{
 father = i;
 flag = true;
 }
 }
 }
 }
 
 static void mutation(List<individual> population){
 for(int i=0; i<individualSize; i++){
 double p = Math.random();
 if(p<mutationProbablility){
 int cnt = (int) (Math.random()*geneLength);
 while(cnt>0){
 int point = (int) (Math.random()*(geneLength-1));
 population.get(i).gene[point] = 1-population.get(i).gene[point];
 cnt -= 1;
 }
 }
 }


 for(int i=0; i<geneLength; i++){
 int count = 0;
 for(int j=0; j<individualSize; j++){
 count += population.get(j).gene[i];
 }


 if(count<3 || count>(individualSize-3)){
 int cn = (int) (Math.random()*individualSize);
 population.get(cn).gene[i] = 1-population.get(cn).gene[i];
 int cn1 = (int) (Math.random()*individualSize);
 while(cn1==cn){
 cn1 = (int) (Math.random()*individualSize);
 }
 population.get(cn1).gene[i] = 1-population.get(cn1).gene[i];
 }
 }


 }
 
 public static void main(String[] args) {
 int ca = 0;
 List<individual> population0 = initPop();
 List<individual> population1 = initPop();
 updatefit(population0);
 updatefit(population1);
 List<individual> bestList0 = new ArrayList<individual>();
 List<individual> bestList1 = new ArrayList<individual>();
 while(ca < maxEvolve){
 List<individual> newP0 = selectPop(population0);
 List<individual> newP1 = selectPop(population1);
 crossover(newP0);
 mutation(newP0);
 crossover(newP1);
 mutation(newP1);
 population0 = newP0;
 population1 = newP1;
 updatefit(population0);
 updatefit(population1);
 bestList0.add(bestIndividual(population0));
 bestList1.add(bestIndividual0(population1));
 ca++;
 System.out.println();
 for(int i=0; i<individualSize; i++){
 System.out.print("基因型："+Arrays.toString(population0.get(i).gene)+" ");
 System.out.println("函数值："+population0.get(i).fit);
 }
 }
 System.out.println("结果");
 individual P0 = bestIndividual(population0);
 System.out.print("基因型："+Arrays.toString(P0.gene)+" ");
 System.out.println("函数最大值："+P0.fit);
 
 individual P1 = bestIndividual0(population1);
 System.out.print("基因型："+Arrays.toString(P1.gene)+" ");
 System.out.println("函数最小值："+P1.fit);
 }
}