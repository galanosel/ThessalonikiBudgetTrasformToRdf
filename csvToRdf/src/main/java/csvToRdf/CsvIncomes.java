package csvToRdf;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.vocabulary.RDF;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;
import ontology.Ontology;
import utils.HelperMethods;

/**
 *
 * @author giorgos, lefteris
 */
public class CsvIncomes {

    public void executeCsvIncomes() {

        //Generic Declarations      
        HelperMethods hm = new HelperMethods();
        Integer year = hm.getCurrentYear();
        String currentDate = hm.getCurrentDate();

        // CSV Declarations
        String splitBy = ";";
        BufferedReader br = null;
        String line = "";
        File csvFileToRead = new File("C:/Users/Lefteris/testForThessProject/2016_03_31_23.29income.csv");
        System.out.println("Input -income csv file: " + csvFileToRead.getName());

        // RDF Declarations
        Model model = ModelFactory.createDefaultModel();
        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        InfModel infModel = ModelFactory.createInfModel(reasoner, model);

        model.setNsPrefix("elod", Ontology.elodPrefix);
        model.setNsPrefix("gr", Ontology.goodRelationsPrefix);
        model.setNsPrefix("dcterms", Ontology.dctermsPrefix);

        try {
            int i = 1;
            br = new BufferedReader(new InputStreamReader(new FileInputStream(csvFileToRead), "UTF16"));
            while (((line = br.readLine()) != null)) {

                i++;
                String[] dataGov = line.split(splitBy);
                String kae = dataGov[1];
                String subject = dataGov[2];
                String budgetformat = dataGov[4];
                String confirmed = dataGov[5];
                String collected = dataGov[6];

                if (!kae.contains("Κ")) {

                    //kae division
                    String customKae = kae;
                    String oneDigit = kae.substring(0, 1);
                    String twoDigit = kae.substring(0, 2);
                    String threeDigit = kae.substring(0, 3);
                    String fourDigit = kae.substring(0, 4);
                    String fifthLevelOfKae1 = kae.substring(5, 7);
                    String fifthLevelOfKae2 = kae.substring(8, 10);

                    //read file name for date
                    String fileName = csvFileToRead.getName();
                    String dateFormat = fileName.substring(0, 10);
                    String dateFormatted = dateFormat.replace("_", "-");
                    String dateFormatNew = dateFormat.substring(5, 10);
                    List<String> items = Arrays.asList(dateFormatNew.split("_"));
                    String dateuri = items.get(1) + "-" + items.get(0);

                    //format financial items
                    String collect = collected.replace(".", "");
                    String collectNew = collect.replace(",", ".");

                    String budget2 = budgetformat.replace(".", "");
                    String budgettNew = budget2.replace(",", ".");

                    String approval2 = confirmed.replace(".", "");
                    String approvalNew = approval2.replace(",", ".");

                    //Creation of Resources
                    Resource instanceKAE = infModel.createResource(Ontology.instancePrefix + "KAE/"
                            + year.toString() + "/Income/" + oneDigit + "/" + twoDigit + "/" + threeDigit
                            + "/" + fourDigit);

                    Resource instanceKAECustom = infModel.createResource(Ontology.instancePrefix + "KAE/"
                            + year.toString() + "/Income/" + oneDigit + "/" + twoDigit + "/" + threeDigit
                            + "/" + fourDigit + "/" + fifthLevelOfKae1 + "/" + fifthLevelOfKae2);

                    Resource instanceBudgetItem = infModel.createResource(Ontology.instancePrefix + "BudgetItem/"
                            + year.toString() + "/" + dateuri + "/"
                            + oneDigit + "/" + twoDigit + "/" + threeDigit + "/" + fourDigit + "/"
                            + fifthLevelOfKae1 + "/" + fifthLevelOfKae2 + "/"
                            + i);

                    Resource instanceSpendingItem = infModel.createResource(Ontology.instancePrefix + "SpendingItem/"
                            + year.toString() + "/" + dateuri + "/"
                            + oneDigit + "/" + twoDigit + "/" + threeDigit + "/" + fourDigit + "/"
                            + fifthLevelOfKae1 + "/" + fifthLevelOfKae2 + "/"
                            + i);

                    Resource instanceExpenseItem = infModel.createResource(Ontology.instancePrefix + "ExpenseApprovalItem/"
                            + year.toString() + "/" + dateuri + "/"
                            + oneDigit + "/" + twoDigit + "/" + threeDigit + "/" + fourDigit + "/"
                            + fifthLevelOfKae1 + "/" + fifthLevelOfKae2 + "/"
                            + i);

                    Resource instanceSpendingExpLine = infModel.createResource(Ontology.instancePrefix + "ExpenditureLine/"
                            + "SpendingItem/" + year.toString() + "/" + dateuri + "/"
                            + oneDigit + "/" + twoDigit + "/" + threeDigit + "/" + fourDigit + "/"
                            + fifthLevelOfKae1 + "/" + fifthLevelOfKae2 + "/"
                            + i);

                    Resource instanceExpenseExpLine = infModel.createResource(Ontology.instancePrefix + "ExpenditureLine/"
                            + "ExpenseApprovalItem/" + year.toString() + "/" + dateuri + "/"
                            + oneDigit + "/" + twoDigit + "/" + threeDigit + "/" + fourDigit + "/"
                            + fifthLevelOfKae1 + "/" + fifthLevelOfKae2 + "/"
                            + i);

                    Resource instanceBudgetUps = infModel.createResource(Ontology.instancePrefix + "UnitPriceSpecification/"
                            + "BudgetItem/" + year.toString() + "/" + dateuri + "/"
                            + oneDigit + "/" + twoDigit + "/" + threeDigit + "/" + fourDigit + "/"
                            + fifthLevelOfKae1 + "/" + fifthLevelOfKae2 + "/"
                            + i);

                    Resource instanceSpendingUps = infModel.createResource(Ontology.instancePrefix + "UnitPriceSpecification/"
                            + "SpendingItem/" + year.toString() + "/" + dateuri + "/"
                            + oneDigit + "/" + twoDigit + "/" + threeDigit + "/" + fourDigit + "/"
                            + fifthLevelOfKae1 + "/" + fifthLevelOfKae2 + "/"
                            + i);

                    Resource instanceExpenseUps = infModel.createResource(Ontology.instancePrefix + "UnitPriceSpecification/"
                            + "ExpenseApprovalItem/" + year.toString() + "/" + dateuri + "/"
                            + oneDigit + "/" + twoDigit + "/" + threeDigit + "/" + fourDigit + "/"
                            + fifthLevelOfKae1 + "/" + fifthLevelOfKae2 + "/"
                            + i);

                    Resource instanceCurrency = infModel.createResource("http://linkedconomy.org/resource/Currency/EUR");

                    Resource instanceOrganization = infModel.createResource(
                            "http://linkedeconomy.org/resource/Organization/998082845");

                    //Resources' s type declarations 
                    infModel.add(instanceKAECustom, RDF.type, Ontology.customKaeResource);
                    infModel.add(instanceBudgetItem, RDF.type, Ontology.budgetItemResource);
                    infModel.add(instanceSpendingItem, RDF.type, Ontology.spendingItemResource);
                    infModel.add(instanceExpenseItem, RDF.type, Ontology.expenseItemResource);
                    infModel.add(instanceSpendingExpLine, RDF.type, Ontology.expLineResource);
                    infModel.add(instanceExpenseExpLine, RDF.type, Ontology.expLineResource);
                    infModel.add(instanceSpendingUps, RDF.type, Ontology.upsResource);
                    infModel.add(instanceBudgetUps, RDF.type, Ontology.upsResource);
                    infModel.add(instanceExpenseUps, RDF.type, Ontology.upsResource);
                    infModel.add(instanceCurrency, RDF.type, Ontology.currencyResource);

                    //Properties addition to Resources
                    instanceSpendingItem.addProperty(Ontology.hasExpenditureLine, instanceSpendingExpLine);
                    instanceExpenseItem.addProperty(Ontology.hasExpenditureLine, instanceExpenseExpLine);

                    instanceBudgetItem.addProperty(Ontology.seller, instanceOrganization);
                    instanceSpendingExpLine.addProperty(Ontology.seller, instanceOrganization);
                    instanceExpenseExpLine.addProperty(Ontology.seller, instanceOrganization);

                    instanceBudgetItem.addProperty(Ontology.price, instanceBudgetUps);
                    instanceSpendingExpLine.addProperty(Ontology.amount, instanceSpendingUps);
                    instanceExpenseExpLine.addProperty(Ontology.amount, instanceExpenseUps);

                    instanceBudgetItem.addProperty(Ontology.hasKae, instanceKAE);
                    instanceSpendingExpLine.addProperty(Ontology.hasKae, instanceKAE);
                    instanceExpenseExpLine.addProperty(Ontology.hasKae, instanceKAE);

                    instanceBudgetItem.addProperty(Ontology.hasCustomKae, instanceKAECustom);
                    instanceSpendingExpLine.addProperty(Ontology.hasCustomKae, instanceKAECustom);
                    instanceExpenseExpLine.addProperty(Ontology.hasCustomKae, instanceKAECustom);

                    instanceBudgetItem.addProperty(Ontology.issued, dateFormatted, XSDDatatype.XSDdateTime);
                    instanceSpendingItem.addProperty(Ontology.issued, dateFormatted, XSDDatatype.XSDdateTime);
                    instanceExpenseItem.addProperty(Ontology.issued, dateFormatted, XSDDatatype.XSDdateTime);

                    instanceBudgetItem.addProperty(Ontology.subject, subject, XSDDatatype.XSDstring);
                    instanceSpendingItem.addProperty(Ontology.subject, subject, XSDDatatype.XSDstring);
                    instanceExpenseItem.addProperty(Ontology.subject, subject, XSDDatatype.XSDstring);

                    instanceBudgetItem.addProperty(Ontology.financialYear, year.toString(), XSDDatatype.XSDgYear);
                    instanceSpendingItem.addProperty(Ontology.financialYear, year.toString(), XSDDatatype.XSDgYear);
                    instanceExpenseItem.addProperty(Ontology.financialYear, year.toString(), XSDDatatype.XSDgYear);

                    instanceBudgetUps.addProperty(Ontology.hasCurrencyValue, String.valueOf(budgettNew), XSDDatatype.XSDfloat);
                    instanceSpendingUps.addProperty(Ontology.hasCurrencyValue, String.valueOf(collectNew), XSDDatatype.XSDfloat);
                    instanceExpenseUps.addProperty(Ontology.hasCurrencyValue, String.valueOf(approvalNew), XSDDatatype.XSDfloat);

                    instanceBudgetUps.addProperty(Ontology.hasCurrency, instanceCurrency);
                    instanceSpendingUps.addProperty(Ontology.hasCurrency, instanceCurrency);
                    instanceExpenseUps.addProperty(Ontology.hasCurrency, instanceCurrency);

                    instanceKAECustom.addProperty(Ontology.kae, String.valueOf(customKae), XSDDatatype.XSDstring);
                }

            }

        } catch (Exception err) {
            System.out.println("Exception caught: " + err.getMessage());
        }
        try {
            BufferedWriter fout = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("C:/Users/Lefteris/testForThessProject/rdf/income" + currentDate + ".rdf"), "UTF8"));
            model.write(fout);
            System.out.println("Transformation of income csv to rdf file completed!!!!");
        } catch (IOException e) {
            System.out.println("Exception caught" + e.getMessage());
        }

    }

}
