import edu.stanford.nlp.pipeline.*;
import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.io.BufferedWriter;



import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.PropertiesUtils;
import edu.stanford.nlp.naturalli.NaturalLogicAnnotations;



import edu.stanford.nlp.coref.CorefCoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.io.*;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.util.*;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;


import edu.stanford.nlp.ie.machinereading.structure.MachineReadingAnnotations; 
import edu.stanford.nlp.ie.machinereading.structure.MachineReadingAnnotations.RelationMentionsAnnotation; 
import edu.stanford.nlp.ie.machinereading.structure.RelationMention; 


public class AffordancePipeline {
    
    class Full_Sentence
    {
        public String Sentence; 
        public String Affordances;  
        public String Object; 
     };

    public static void main(String[] args) {

       
        List<String> full_sentences = new ArrayList<>();
        List<String[]> answers = new ArrayList<>();

        String filename = "/Users/jenniewerner/Desktop/NLP_Project/input.txt";

        //read file into stream, try-with-resources
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {

            stream.forEach(item->{
                String delims = "[/]";
                String[] tokens = item.split(delims);
                full_sentences.add(tokens[0]);
                String [] a = new String[2];
                a[0] = tokens[1];
                a[1] = tokens[2];
                answers.add(a);

            });


        } catch (IOException e) {
            e.printStackTrace();
        }
        
        System.out.println(full_sentences);


        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
        Properties props = new Properties();
        //Properties props = StringUtils.propFileToProperties("test/roth.properties");
        props.setProperty("annotators", "tokenize, ssplit, pos, parse, lemma, ner, depparse, relation"); //natlog, openie");
        props.setProperty("sup.relation.model", "/Users/jenniewerner/Desktop/NLP_Project/tmp/roth_relation_model_pipeline.ser");

        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // read some text in the text variable
        String text = String.join(" ", full_sentences);
        

        System.out.println(text);

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);

        // run all Annotators on this text
        pipeline.annotate(document);

        // these are all the sentences in this document
        // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        System.out.println(sentences);

        //Get the file reference
        String outputFile = "/Users/jenniewerner/Desktop/NLP_Project/output.txt";
         
        String output = "";

        Integer sent_index = 0;
        for(CoreMap sentence: sentences) {
            Integer index = 0;
            Integer affordance = -1;
            Integer object = -1;

            for (CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                // this is the text of the token
                String word = token.get(CoreAnnotations.TextAnnotation.class);
                // this is the POS tag of the token
                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                // this is the NER label of the token
                String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                
                System.out.println(ne + "\t" + index + "\t" + pos + "\t" + word);
                
                if(word.equals(answers.get(sent_index)[0])){
                    affordance = index;
                }
                if(word.equals(answers.get(sent_index)[1])){
                    object = index;
                }
                               
                index = index + 1;

              }

              System.out.println("\n" + affordance + "\t" + object + "\t" + "affordance" + "\n");
              sent_index = sent_index + 1;


            // List<RelationMention> relations = sentence.get(MachineReadingAnnotations.RelationMentionsAnnotation.class);

            // SemanticGraph deps = sentence.get(SemanticGraphCoreAnnotations.BasicDependenciesAnnotation.class);
            // System.out.println(deps.toString(SemanticGraph.OutputFormat.LIST));
            // Collection<RelationTriple> triples = sentence.get(NaturalLogicAnnotations.RelationTriplesAnnotation.class);

            //   // Print the triples
            //   for (RelationTriple triple : triples) {
            //     System.out.println(triple.confidence + "\t" +
            //         triple.subjectLemmaGloss() + "\t" +
            //         triple.relationLemmaGloss() + "\t" +
            //         triple.objectLemmaGloss());
            //   }

            // for (SemanticGraphEdge edge : deps.edgeIterable() ) {
            //     String rel = edge.getRelation().toString();
            //     if ("dobj".equals(rel) || "acl:relcl".equals(rel)) {
            //         System.out.println("object:  " + edge.getDependent().word());
            //         System.out.println("affordances:  " + edge.getGovernor().word());

            //     }

            // }  
   

        }

    }

}
