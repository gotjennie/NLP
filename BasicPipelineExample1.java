import edu.stanford.nlp.pipeline.*;
import java.util.*;
import java.io.*;


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


public class BasicPipelineExample1 {

    public static void main(String[] args) {

        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
        Properties props = new Properties();
        //Properties props = StringUtils.propFileToProperties("test/roth.properties");
        props.setProperty("annotators", "tokenize, ssplit, pos, parse, lemma, ner, depparse, relation"); //natlog, openie");
        props.setProperty("sup.relation.model", "/Users/jenniewerner/Desktop/NLP_Project/tmp/roth_relation_model_pipeline.ser");

        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // read some text in the text variable
        String text = "James killed Frank. Jennie was born in America. No group was mentioned in the stories and no one called the Star to claim   responsibility, said Managing John/Peck. The cat killed the dog.";

        //"I ate some pie. Alice was beginning to get very tired of sitting by her sister on the bank, and of having nothing to do: once or twice she had peeped into the book her sister was reading, but it had no pictures or conversations in it, ‘and what is the use of a book,’ thought Alice ‘without pictures or conversations?’";  //The boy threw the ball far away. We drove away from home in a car";

        //"I've only been to Boltwood once for brunch on a Saturday morning and overall, I was very impressed during my visit. Boltwood's brunch menu is unique and diverse. I had a hard time choosing since there were many offerings, but I decided to try the Boltwood burger & crispy potatoes, thanks to Yelp. I was debating between the breakfast sausage and the burger. I definitely was not disappointed by the food. The crispy potatoes were crispy to perfection and the burger was juicy with a delicious tangy sauce. The potatoes were a tad bit too salty for my taste, but they were all devoured in one sitting. I really enjoyed the decor and vibe of Boltwood. Everything was aesthetically pleasing and the glass windows let in a good amount of sunlight.  The service was also great and I felt they were very attentive and helpful. I am very interested in checking out their dinner menu and maybe their $1 oysters on Wednesdays.";

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);

        // run all Annotators on this text
        pipeline.annotate(document);

        // these are all the sentences in this document
        // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        System.out.println(sentences);

        for(CoreMap sentence: sentences) {

            List<RelationMention> relations = sentence.get(MachineReadingAnnotations.RelationMentionsAnnotation.class);

            if(relations != null){
                System.err.println("Extracted the following relations:");
                for(RelationMention r: relations){
                    if(! r.getType().equals(RelationMention.UNRELATED)){
                        System.out.println(r);
                    }
                 }        
            }

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
