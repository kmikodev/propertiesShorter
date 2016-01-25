/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author kmiko
 */
public class JavaApplication2 {

    static String RUTA = "/home/kmiko/.mozilla/firefox/2hzyl0vl.default/extensions/JavaApplication2/src/result/";
    static String FILE_1 = "/home/kmiko/.mozilla/firefox/2hzyl0vl.default/extensions/JavaApplication2/src/application_es.properties";
    static String FILE_2 = "/home/kmiko/.mozilla/firefox/2hzyl0vl.default/extensions/JavaApplication2/src/application_en.properties";
    static List<PropertieComplete> pOrigen1 = new ArrayList<PropertieComplete>();
    static List<Propertie> pRepetidos1 = new ArrayList<Propertie>();
    static List<Propertie> pSinPareja1 = new ArrayList<Propertie>();
    static List<String> pComentarios1 = new ArrayList<String>();
    static List<Propertie> pResult1 = new ArrayList<Propertie>();
    static List<PropertieComplete> pOrigen2 = new ArrayList<PropertieComplete>();
    static List<Propertie> pRepetidos2 = new ArrayList<Propertie>();
    static List<Propertie> pSinPareja2 = new ArrayList<Propertie>();
    static List<String> pComentarios2 = new ArrayList<String>();
    static List<Propertie> pResult2 = new ArrayList<Propertie>();
    static Integer tot1Origen[] = {0};
    static Integer tot2Origen[] = {0};

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        //llenar listas
        pOrigen1 = readFile(FILE_1, pComentarios1, tot1Origen);
        pOrigen2 = readFile(FILE_2, pComentarios2, tot2Origen);

        //llenar lista repetidos
        pRepetidos1 = buscarRepetido(pOrigen1);
        pRepetidos2 = buscarRepetido(pOrigen2);

        formarFinalYSolos(pOrigen1, pOrigen2);

        imprimirFicherosP(pResult1, "p1.properties");
        imprimirFicherosP(pResult2, "p2.properties");

        imprimirFicherosP(pRepetidos1, "r1.properties");
        imprimirFicherosP(pRepetidos2, "r2.properties");

        imprimirFicherosP(pSinPareja1, "sP1.properties");
        imprimirFicherosP(pSinPareja2, "sP2.properties");

        imprimirComentarios(pComentarios1, "cP1.properties");
        imprimirComentarios(pComentarios2, "cP2.properties");

        comprobar(pRepetidos1, pComentarios1, pResult1, tot1Origen, pSinPareja1);

        comprobar(pRepetidos2, pComentarios2, pResult2, tot2Origen, pSinPareja2);

    }

    static void comprobar(List<Propertie> pRepetidos, List<String> pComentarios, List<Propertie> pResult, Integer[] totOrigen, List<Propertie> pSinPareja) {
        if (pRepetidos.size() + pComentarios.size() + pResult.size() == totOrigen[0]) {
            System.out.println("Fichero 1 correcto");
        } else {
            System.out.println("ResultSize" + (pRepetidos.size() + pComentarios.size() + pResult.size() + pSinPareja.size()) + " tot " + totOrigen[0]);
        }
    }

    static void imprimirComentarios(List<String> pComentarios, String name) throws IOException {
        System.out.println("imprimiendoP");
        imprimir(pComentarios, name);
    }

    static void imprimirFicherosP(List<Propertie> p, String name) throws IOException {
        System.out.println("imprimiendoP");
        List<String> lines = new ArrayList<String>();
        for (Propertie pro : p) {
            if (pro.getClave() != null && pro.getValor() != null && pro.getClave().length() > 0 && pro.getValor().length() > 0) {
                lines.add(pro.toString());
            }
        }
        imprimir(lines, name);
    }

    static void imprimirFicherosPC(List<PropertieComplete> p, String name) throws IOException {
        System.out.println("imprimiendoPC");
        List<String> lines = new ArrayList<String>();
        for (PropertieComplete pro : p) {
            if (pro.getClave() != null && pro.getValor() != null && pro.getClave().length() > 0 && pro.getValor().length() > 0) {
                lines.add(pro.toString());
            }
        }

        imprimir(lines, name);
    }

    static void imprimir(List<String> lines, String name) throws IOException {

        try {
            Path file = Paths.get(RUTA + name);
            Files.write(file, lines, Charset.forName("UTF-8"));
        } catch (Exception e) {
            System.out.println(e.getCause());
        }

    }

    public static void formarFinalYSolos(List<PropertieComplete> pO1, List<PropertieComplete> pO2) {
        for (int j = 0; j < pO1.size(); j++) {
            if (!pO1.get(j).isBuscado() && !pO1.get(j).isRepetido()) {
                int encontrado = buscar(pO1.get(j).getClave(), pO2);
                if (encontrado > -1) {
                    pResult1.add(pO1.get(j));
                    pO1.get(j).setBuscado(true);
                    pResult2.add(pO2.get(encontrado));
                    pO2.get(encontrado).setBuscado(true);
                } else {
                    pSinPareja1.add(pO1.get(j));
                    pO1.get(j).setRepetido(true);
                }
            }
        }
        for (PropertieComplete p2 : pO2) {
            if (!p2.isBuscado()) {
                pSinPareja2.add(p2);
            }
        }
    }

    static int buscar(String clave, List<PropertieComplete> pOrigen) {
        int encontrado = -1;
        for (int j = 0; j < pOrigen.size(); j++) {
            if (!pOrigen.get(j).isBuscado() && !pOrigen.get(j).isRepetido()) {
                if (pOrigen.get(j).getClave().trim().compareTo(clave.trim()) == 0) {
                    encontrado = j;
                    break;
                }
            }
        }
        return encontrado;
    }

    public static List<Propertie> buscarRepetido(List<PropertieComplete> pOrigen) {
        List<Propertie> result = new ArrayList<Propertie>();

        for (int j = 0; j < pOrigen.size(); j++) {
            if (!pOrigen.get(j).isBuscado() && !pOrigen.get(j).isRepetido()) {
                pOrigen.get(j).setBuscado(true);
                for (int i = 0; i < pOrigen.size(); i++) {
                    PropertieComplete p = pOrigen.get(i);
                    if (!p.isBuscado() && !p.isRepetido()) {
                        if (p.getClave().compareTo(pOrigen.get(j).getClave()) == 0) {
                            result.add(p);
                            p.setBuscado(true);
                            p.setRepetido(true);
                        }
                    }

                }
                pOrigen.get(j).setBuscado(false);
            }
        }
        return result;
    }

    public static List<PropertieComplete> readFile(String ruta, List<String> pComentarios, Integer[] totLinea) {
        BufferedReader br = null;
        List<PropertieComplete> result = new ArrayList<PropertieComplete>();
        try {

            String sCurrentLine;

            br = new BufferedReader(new FileReader(ruta));

            while ((sCurrentLine = br.readLine()) != null) {
                totLinea[0] += 1;
                if (sCurrentLine.split("=").length > 1) {
                    int primerIgual = sCurrentLine.trim().indexOf("=");
                    if (!sCurrentLine.startsWith("#")) {
                        String clave = sCurrentLine.trim().substring(0, primerIgual);
                        String valor = sCurrentLine.trim().substring(primerIgual + 1);

                        PropertieComplete p = new PropertieComplete(clave, valor);
                        result.add(p);
                    } else {
                        pComentarios.add(sCurrentLine);
                    }
                } else {
                    pComentarios.add(sCurrentLine);
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    public static class Propertie {

        private String clave;
        private String valor;

        public String getClave() {
            return clave;
        }

        public void setClave(String clave) {
            this.clave = clave;
        }

        public String getValor() {
            return valor;
        }

        public void setValor(String valor) {
            this.valor = valor;
        }

        public Propertie() {

        }

        @Override
        public String toString() {
            return clave + "=" + valor;
        }

    }

    public static class PropertieComplete extends Propertie {

        private boolean buscado;
        private boolean repetido;

        public PropertieComplete() {
        }

        public PropertieComplete(String clave, String valor) {
            this.setClave(clave);
            this.setValor(valor);
            this.buscado = false;
            this.repetido = false;

        }

        public boolean isBuscado() {
            return buscado;
        }

        public void setBuscado(boolean buscado) {
            this.buscado = buscado;
        }

        public boolean isRepetido() {
            return repetido;
        }

        public void setRepetido(boolean repetido) {
            this.repetido = repetido;
        }

        @Override
        public String toString() {
            return super.toString();
        }

    }
}
