/**
 * Class Name: <ComplaintNewspaperControllerTest>
 * @author: <KarlTan>
 * Date: <8/16/2014>
 * Requirement/Project Name: <SPH>
 * @description<Test Class for ComplaintNewspaperController>
 */

@isTest (seeAllData= True)
public class ComplaintNewspaperControllerTest {


    ///static Account acc;    
    static testMethod void testSingleVendorSub(){
        
        //Create a new user with Standard User Profile

        User u = new User(
            Alias = 'stndrd',
            Email='standarduser@test.com', 
            EmailEncodingKey='UTF-8',
            LastName='Test',
            LanguageLocaleKey='en_US', 
            LocaleSidKey='en_US',
            ProfileId = (Id) ConstantsSLB.getKeyId('Profile_Standard User'), 
            TimeZoneSidKey='America/Los_Angeles',
            UserName='standard@test.com'
            );
        
      //  System.assertEquals('', String.valueOf(u));

        //The test will run as the user created.
        System.runAs(u) {
            //create a new case record
            
            Account acc = new Account(Name = 'Test Account', RecordTypeId = ConstantsSLB.getKeyId('Account_Vendor Subscriber'));
            insert acc;

            List<Contact> con = TestDataFactory.createContact(1);
            con[0].Phone = '1234567';
            insert con;
            
            List<Package__c> pkg = TestDataFactory.createPackage(1);
            pkg[0].Subscription_Period__c = '15';            

            insert pkg;

            List<Subscription_Vendor__c> subVen = TestDataFactory.createSubscriptionVendor(1);
            insert subVen;  

                        
            Case testCase = new Case();
            testCase.AccountId = acc.id;
            testCase.Origin = 'Phone';
            testCase.Priority = 'Medium';
            testCase.Date_Replaced__c = system.today();
            testCase.Status = 'New';
            
            insert testCase;
             
            Case_Vendor_Subscription__c cVenSub = new Case_Vendor_Subscription__c();
            cVenSub.Subscription_Vendor__c = subVen[0].id;
            cVenSub.Complaint_From__c = system.today();
            cVenSub.Complaint_To__c = system.today();
            cVenSub.Case__c = testCase.id;
            insert cVenSub;          
               
                                                                 
                                    
            Test.startTest();
            
            //initialize page and controllers
            PageReference pgRef = Page.ComplaintMagazineEditPage;
            Test.setCurrentPage(pgRef);
            ApexPages.currentPage().getParameters().put('id', testCase.Id);
            ApexPages.StandardController stdCtrlr = new ApexPages.StandardController(testCase);
            ComplaintNewspaperController compNewsCont = new ComplaintNewspaperController (stdCtrlr);
           
            compNewsCont.objcase.AccountId = acc.Id;
            compNewsCont.objcase.Origin = 'Phone';
            compNewsCont.objcase.Priority = 'Medium';
            compNewsCont.objcase.Date_Replaced__c = system.today();
            compNewsCont.objcase.Status = 'New';
           
            compNewsCont.save();
            

            compNewsCont.addRow();
            compNewsCont.cancel();
            //compMagextn.removeRows();
                                                
            Test.stopTest();
      
        }           
    }

    ///static Account acc;    
    static testMethod void testSingleDirectSub(){
        
        //Create a new user with Standard User Profile

        User u = new User(
            Alias = 'stndrd',
            Email='standarduser@test.com', 
            EmailEncodingKey='UTF-8',
            LastName='Test',
            LanguageLocaleKey='en_US', 
            LocaleSidKey='en_US',
            ProfileId = (Id) ConstantsSLB.getKeyId('Profile_Standard User'), 
            TimeZoneSidKey='America/Los_Angeles',
            UserName='standard@test.com'
            );
        
      //  System.assertEquals('', String.valueOf(u));

        //The test will run as the user created.
        System.runAs(u) {
            //create a new case record
            
            Account acc = new Account(Name = 'Test Account', RecordTypeId = ConstantsSLB.getKeyId('Account_Direct Corporate'));
            insert acc;
            
            List<Contact> con = TestDataFactory.createContact(1);
            con[0].Phone = '1234567';
            insert con;
            
            List<Package__c> pkg = TestDataFactory.createPackage(1);
            pkg[0].Subscription_Period__c = '15'; 
            insert pkg;

			List<Publication__c> pub = TestDataFactory.createPublication(1);
			pub.Cover_Price_Mon__c = '1';
			pub.Cover_Price_Tue__c = '2';
			pub.Cover_Price_Wed__c = '3';
			pub.Cover_Price_Thu__c = '4';
			pub.Cover_Price_Fri__c = '5';
			pub.Cover_Price_Sat__c = '6';
			pub.Cover_Price_Sun__c = '7';
			pub.Product_Family__c = 'Straits Times';
			pub.Publish_Pattern__c = 'Weekdays';
			insert pub;
						
			List<zqu__ProductRatePlan__c> prp = TestDataFactory.createProductRatePlan(1);
			insert prp;
			
			List<zqu__ProductRatePlanCharge__c> prpc = TestDataFactory.createProductRatePlanCharge(1);
			prpc.zqu__Type__c = 'Recurring';
			prpc.zqu__UOM__c = 'Issues';
			prpc.zqu__RecurringPeriod__c = 'Month';
			prpc.PublicationCode__c = 'ST';
			insert prpc;

            List<Zuora__Subscription__c> zSubs =  TestDataFactory.createSubscription(1);
            zSubs[0].Subscriber_Contact__c = con[0].id;
            zSubs[0].Main_Package_1__c = pkg[0].id;
            insert zSubs;  

			List<Zuora__SubscriptionProductCharge__c> spc = TestDataFactory.createSubProductCharge(1);
			spc.Zuora__Account__c = 'Eugene Acc';
			spc.Zuora__Price__c = '20.65';
			spc.Zuora__UOM__c = 'Issues';
			spc.Zuora__BillingPeriod__c = 'Month';
			spc.Zuora__RatePlanName__c = 'THE STRAITS TIMES PRINT - Monthly Prepaid';
			spc.Zuora__ProductSKU__c = 'SKU-00000083';
			spc.Zuora__ProductName__c = 'THE STRAITS TIMES PRINT';
			insert spc;
			                        
            Case testCase = new Case();
            testCase.AccountId = acc.id;
            testCase.Origin = 'Phone';
            testCase.Priority = 'Medium';
            testCase.Date_Replaced__c = system.today();
            testCase.Status = 'New';
            
            insert testCase;
             
            Case_Subscription__c cSub = new Case_Subscription__c();
            cSub.Subscription__c = zSubs[0].id;
            cSub.Complaint_From__c = system.today();
            cSub.Complaint_To__c = system.today();
            cSub.Case__c = testCase.id;
            insert cSub;          
               
                                                                                                     
            Test.startTest();
            
            //initialize page and controllers
            PageReference pgRef = Page.ComplaintMagazineEditPage;
            Test.setCurrentPage(pgRef);
            ApexPages.currentPage().getParameters().put('id', testCase.Id);
            ApexPages.StandardController stdCtrlr = new ApexPages.StandardController(testCase);
            ComplaintNewspaperController compNewsCont = new ComplaintNewspaperController (stdCtrlr);
                       
            compNewsCont.objcase.AccountId = acc.Id;
            compNewsCont.objcase.Origin = 'Phone';
            compNewsCont.objcase.Priority = 'Medium';
            compNewsCont.objcase.Date_Replaced__c = system.today();
            compNewsCont.objcase.Status = 'New';
           
            compNewsCont.save();
            

            compNewsCont.addRow();
            compNewsCont.cancel();
            //compMagextn.removeRows();
                                                
            Test.stopTest();
      
        }           
    }

    static testMethod void testCreateNew(){
        
        //Create a new user with Standard User Profile

        User u = new User(
            Alias = 'stndrd',
            Email='standarduser@test.com', 
            EmailEncodingKey='UTF-8',
            LastName='Test',
            LanguageLocaleKey='en_US', 
            LocaleSidKey='en_US',
            ProfileId = (Id) ConstantsSLB.getKeyId('Profile_Standard User'), 
            TimeZoneSidKey='America/Los_Angeles',
            UserName='standard@test.com'
            );
        
      //  System.assertEquals('', String.valueOf(u));

        //The test will run as the user created.
        System.runAs(u) {
            //create a new case record
            
            Account acc = new Account(Name = 'Test Account', RecordTypeId = ConstantsSLB.getKeyId('Account_Vendor Subscriber'));
            insert acc;

            List<Contact> con = TestDataFactory.createContact(1);
            con[0].Phone = '1234567';
            insert con;
            
            List<Package__c> pkg = TestDataFactory.createPackage(1);
            pkg[0].Subscription_Period__c = '15';            

            insert pkg;

            List<Subscription_Vendor__c> subVen = TestDataFactory.createSubscriptionVendor(1);
            insert subVen;  

                        
            Case testCase = new Case();
            testCase.AccountId = acc.id;
            testCase.Origin = 'Phone';
            testCase.Priority = 'Medium';
            testCase.Date_Replaced__c = system.today();
            testCase.Status = 'New';
            
            insert testCase;
             
            Case_Vendor_Subscription__c cVenSub = new Case_Vendor_Subscription__c();
            cVenSub.Subscription_Vendor__c = subVen[0].id;
            cVenSub.Complaint_From__c = system.today();
            cVenSub.Complaint_To__c = system.today();
            cVenSub.Case__c = testCase.id;
            insert cVenSub;          
               
                                                                 
                                    
            Test.startTest();
            
            //initialize page and controllers
            PageReference pgRef = Page.ComplaintMagazineEditPage;
            Test.setCurrentPage(pgRef);
            //ApexPages.currentPage().getParameters().put('id', testCase.Id);
            ApexPages.StandardController stdCtrlr = new ApexPages.StandardController(new Case());
            ComplaintNewspaperController compNewsCont = new ComplaintNewspaperController (stdCtrlr);
           
            compNewsCont.objcase.AccountId = acc.Id;
            compNewsCont.objcase.Origin = 'Phone';
            compNewsCont.objcase.Priority = 'Medium';
            compNewsCont.objcase.Date_Replaced__c = system.today();
            compNewsCont.objcase.Status = 'New';
           
            compNewsCont.save();
            

            compNewsCont.addRow();
            compNewsCont.cancel();
            //compMagextn.removeRows();
                                                
            Test.stopTest();
      
        }           
    }
     
}