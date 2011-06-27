//package CodeFinal;
//
//import java.io.File;
//import java.util.ArrayList;
//
//public class Test5 {
//	public static void main(String[] args) {
//
//		//Parser
//		int compteur = 1;
//		ArrayList<EnsembleFaces> vectBatiment = new ArrayList<EnsembleFaces>();
//		while(new File("batiment" + compteur + ".stl").exists()) {
//			vectBatiment.add(new EnsembleFaces(Parser.readSTLA(new File("batiment - " + compteur + ".stl"))));
//			System.out.println("Lecture du fichier batiment" + compteur + " termin�e !");
//			System.out.println("Nombre de triangles : " + vectBatiment.get(compteur - 1).size());
//			compteur ++;
//		}
//		
//		ArrayList<EnsembleFaces> vectSurfaces = new ArrayList<EnsembleFaces>();
//		
//		while(!fin) {			
//			//On choisit une face de mur au hasard
//			int i = 0;
//			while(!batiment.get(i).estNormalA(normalSol, errorNormalSol)) {
//				i++;
//				if(i == (batiment.size() - 1)) {
//					fin = true;
//					break;
//				}
//			}
//			
//			Triangle tri = batiment.get(i);
//			
//			//Calcul des orient�s
//			//TODO : permettre de changer le facteur de multiplication
//			EnsembleFaces meshOrientes = batiment.orientesSelon(tri.getNormale(), 20*errorNormalSol);	
//			System.out.println("Taille de meshOrientes : " + meshOrientes.size());
//			
//			//Cr�ation d'un pseudo-index : assignation de voisins � chaque triangle
//			long time = System.nanoTime();
//			Tuilage quad = new Tuilage(meshOrientes.xMin(), meshOrientes.xMax(), meshOrientes.yMin(), meshOrientes.yMax(), meshOrientes.zMin(), meshOrientes.zMax(), 50, 50, 50);
//			quad.addEnsembleFaces(meshOrientes);
//			quad.findNeighbours();
//			System.out.println("Temps �coul� pour l'indexation : " + (System.nanoTime() - time));
//			
//			EnsembleFaces mur = new EnsembleFaces();
//			tri.returnNeighbours(mur);
//			batiment.suppress(mur);
//			
//			vectSurfaces.add(mur);
//		}
//		
//		//Application de l'algorithme : moyenne des triangles
//		int nombre = 0;
//		for(EnsembleFaces ens : vectSurfaces) {
//			nombre += ens.size();
//		}
//		
//		double erreurNombreBlocs = (double)nombre/(double)vectSurfaces.size();
//		ArrayList<EnsembleFaces> murs = new ArrayList<EnsembleFaces>();
//		
//		int compteur = 0;
//		
//		for(EnsembleFaces ens : vectSurfaces) {
//			if(ens.size() > 10*erreurNombreBlocs) {
//				murs.add(ens);
//				Writer.ecrireSurfaceA(new File("mur" + compteur + ".stl"), ens);
//				compteur++;
//			}
//		}
//}
