//
//  ContactsViewController.m
//  Reloved
//
//  Created by Kamal on 31/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import "ContactsViewController.h"
#import "Person.h"
#import "TableCell.h"

@interface ContactsViewController ()

@end

@implementation ContactsViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    tbl_contacts.dataSource = self;
    tbl_contacts.delegate = self;
    
    arrContactList = [[NSMutableArray alloc]init];
    arrContactListBackUp = [[NSMutableArray alloc]init];
    
    lblInviteFreinds.font = FONT_Lato_Bold(20.0f);
}

- (void) viewWillAppear:(BOOL)animated {
    KAppDelegate.MyVC = self;
    [self AccessContacts];
}

// here we get access contacts from device........
- (void) AccessContacts
{
    ABAddressBookRef addressBookRef = ABAddressBookCreateWithOptions(NULL, NULL);
    if (ABAddressBookGetAuthorizationStatus() == kABAuthorizationStatusNotDetermined) {
        ABAddressBookRequestAccessWithCompletion(addressBookRef, ^(bool granted, CFErrorRef error) {
            [self performSelectorInBackground:@selector(getAddressBook) withObject:self];
        });
    } else if (ABAddressBookGetAuthorizationStatus() == kABAuthorizationStatusAuthorized) {
        [self performSelectorInBackground:@selector(getAddressBook) withObject:self];
    }
}

#pragma mark - TableView Delegate and DataSource methods
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return [arrContactList count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    static NSString * cellIdentifier = @"Cell Identification";
    TableCell * cell = [tableView dequeueReusableCellWithIdentifier:cellIdentifier];
    if(cell == nil){
        
        NSArray * nib = [[NSBundle mainBundle] loadNibNamed:@"TableCell" owner:self options:nil];
        for(id object in nib){
            if([object isKindOfClass:[TableCell class]]) {
                cell = (TableCell * ) object;
                cell.selectionStyle = UITableViewCellSelectionStyleNone;
            }
        }
    }
    
    Person *obPerson = [arrContactList objectAtIndex:indexPath.row];
    if (obPerson.flageShowOperation) {
       // [cell showOperations];
    } else {
       // [cell showName_Numbers];
        cell.lblName.text = obPerson.strFullName;
        if([obPerson.strPhoneNumber isEqualToString:@"(null)"]){
            cell.lblNumber.text = @"No number";
            //cell.strPhoneNumber = @"";
        } else {
            cell.lblNumber.text = obPerson.strPhoneNumber;
            //cell.strPhoneNumber = obPerson.strPhoneNumber;
        }
    }
    //cell.delegate = self;
    //cell.indexPath = indexPath;
    
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    cell.backgroundColor = [UIColor clearColor];
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    intSelectedTableIndex = indexPath.row;
    [self ShowDetailsInfo];
}

// here we show contacts Details.....
- (void) ShowDetailsInfo {
    Person * obPerson = [arrContactList objectAtIndex:intSelectedTableIndex];
    CFErrorRef error = NULL;
    ABAddressBookRef obAddressBook = ABAddressBookCreateWithOptions(NULL, &error);
    NSLog(@"obAddressBook--> %@",obAddressBook);
    
    if (obAddressBook != nil) {
        NSArray *allContacts = (__bridge_transfer NSArray *)ABAddressBookCopyArrayOfAllPeople(obAddressBook);
        NSUInteger i = 0;
        for (i = 0; i < [allContacts count]; i++) {
            ABRecordRef contactPerson = (__bridge ABRecordRef)allContacts[i];
            NSString *firstName = (__bridge_transfer NSString *)ABRecordCopyValue(contactPerson, kABPersonFirstNameProperty);
            NSString *lastName =  (__bridge_transfer NSString *)ABRecordCopyValue(contactPerson, kABPersonLastNameProperty);
            ABMultiValueRef multi = ABRecordCopyValue(contactPerson, kABPersonPhoneProperty);
            NSNumber * mobileNumber = (__bridge NSNumber *)ABMultiValueCopyValueAtIndex(multi, 0);
            ABMultiValueRef emails = ABRecordCopyValue(contactPerson, kABPersonEmailProperty);
            
            NSUInteger j = 0;
            NSString * strHomeEmail = @"";
            NSString * strWorkEmail = @"";
            for (j = 0; j < ABMultiValueGetCount(emails); j++) {
                NSString *email = (__bridge_transfer NSString *)ABMultiValueCopyValueAtIndex(emails, j);
                if (j == 0) {
                    strHomeEmail = email;
                } else if (j == 1) {
                    strWorkEmail = email;
                }
            }
           
            if ([obPerson.strPhoneNumber isEqualToString:[NSString stringWithFormat:@"%@",mobileNumber]]) {
                BOOL flage = NO;
                if (firstName != nil) {
                    if ([firstName isEqualToString:obPerson.strFirstName]) {
                        flage = YES;
                    } else {
                        flage = NO;
                    }
                } else {
                    flage = YES;
                }
                
                if (lastName != nil) {
                    if ([lastName isEqualToString:obPerson.strLastName]) {
                        flage = YES;
                    } else {
                        flage = NO;
                    }
                }
                
                if (flage) {
                    personController.displayedPerson = contactPerson;
                    personController = [[ABPersonViewController alloc] init];
                    personController.personViewDelegate = self;
                    personController.allowsEditing = YES;
                    personController.displayedPerson = contactPerson;
                    
                    
                    personController.navigationItem.backBarButtonItem = [[UIBarButtonItem alloc] initWithTitle:NSLocalizedString(@"Cancel",nil) style:UIBarButtonItemStylePlain target:self action:@selector(navigationItemButton)];
                    
                    newNavigationController = [[UINavigationController alloc] initWithRootViewController:personController];
                    [self presentModalViewController:newNavigationController animated:YES];
                    //[delegate HideFooter];
                    break;
                }
            }
        }
    }
}

- (void) navigationItemButton
{
    [newNavigationController dismissViewControllerAnimated:YES completion:^{
    }];
}

// here we get address book........
- (void)getAddressBook
{
    [arrContactList removeAllObjects];// = [[NSMutableArray alloc] init];
    [arrContactListBackUp removeAllObjects];// = [[NSMutableArray alloc] init];
    
    CFErrorRef error = NULL;
    arrContactList = [[NSMutableArray alloc] init];
    ABAddressBookRef obAddressBook = ABAddressBookCreateWithOptions(NULL, &error);
    
     NSLog(@"obAddressBook--> %@",obAddressBook);
    if (obAddressBook != nil) {
        NSArray *allContacts = (__bridge_transfer NSArray *)ABAddressBookCopyArrayOfAllPeople(obAddressBook);
         NSLog(@"allContacts--> %@",allContacts);
        NSUInteger i = 0;
        for (i = 0; i < [allContacts count]; i++) {
            Person * obPerson = [[Person alloc] init];
            
            ABRecordRef contactPerson = (__bridge ABRecordRef)allContacts[i];
            NSString *fullName, *street, *city, *country, *state, * zipCode;
            NSString *fullNameWork, *streetWork, *cityWork, *countryWork, *stateWork, * zipCodeWork;
            NSString *fullNameOther, *streetOther, *cityOther, *countryOther, *stateOther, * zipCodeOther;
            NSString *firstName = (__bridge_transfer NSString *)ABRecordCopyValue(contactPerson, kABPersonFirstNameProperty);
            NSString *lastName =  (__bridge_transfer NSString *)ABRecordCopyValue(contactPerson, kABPersonLastNameProperty);
            
            NSString *ClonekABPersonFirstNameProperty =  (__bridge_transfer NSString *)ABRecordCopyValue(contactPerson, kABPersonFirstNameProperty);
            NSString *ClonekABPersonLastNameProperty =  (__bridge_transfer NSString *)ABRecordCopyValue(contactPerson, kABPersonLastNameProperty);
            NSString *ClonekABPersonMiddleNameProperty =  (__bridge_transfer NSString *)ABRecordCopyValue(contactPerson, kABPersonMiddleNameProperty);
            NSString *ClonekABPersonPrefixProperty =  (__bridge_transfer NSString *)ABRecordCopyValue(contactPerson, kABPersonPrefixProperty);
            NSString *ClonekABPersonSuffixProperty =  (__bridge_transfer NSString *)ABRecordCopyValue(contactPerson, kABPersonSuffixProperty);
            NSString *ClonekABPersonNicknameProperty =  (__bridge_transfer NSString *)ABRecordCopyValue(contactPerson, kABPersonNicknameProperty);
            NSString *ClonekABPersonFirstNamePhoneticProperty =  (__bridge_transfer NSString *)ABRecordCopyValue(contactPerson, kABPersonFirstNamePhoneticProperty);
            NSString *ClonekABPersonLastNamePhoneticProperty =  (__bridge_transfer NSString *)ABRecordCopyValue(contactPerson, kABPersonLastNamePhoneticProperty);
            NSString *ClonekABPersonMiddleNamePhoneticProperty =  (__bridge_transfer NSString *)ABRecordCopyValue(contactPerson, kABPersonMiddleNamePhoneticProperty);
            NSString *ClonekABPersonOrganizationProperty =  (__bridge_transfer NSString *)ABRecordCopyValue(contactPerson, kABPersonOrganizationProperty);
            NSString *ClonekABPersonJobTitleProperty =  (__bridge_transfer NSString *)ABRecordCopyValue(contactPerson, kABPersonJobTitleProperty);
            NSString *ClonekABPersonDepartmentProperty =  (__bridge_transfer NSString *)ABRecordCopyValue(contactPerson, kABPersonDepartmentProperty);
            NSString *ClonekABPersonEmailProperty =  (__bridge_transfer NSString *)ABRecordCopyValue(contactPerson, kABPersonEmailProperty);
            NSString *ClonekABPersonBirthdayProperty =  (__bridge_transfer NSString *)ABRecordCopyValue(contactPerson, kABPersonBirthdayProperty);
            NSString *ClonekABPersonNoteProperty =  (__bridge_transfer NSString *)ABRecordCopyValue(contactPerson, kABPersonNoteProperty);
            NSString *ClonekABPersonCreationDateProperty =  (__bridge_transfer NSString *)ABRecordCopyValue(contactPerson, kABPersonCreationDateProperty);
            NSString *ClonekABPersonModificationDateProperty =  (__bridge_transfer NSString *)ABRecordCopyValue(contactPerson, kABPersonModificationDateProperty);
         
            ABMultiValueRef multi = ABRecordCopyValue(contactPerson, kABPersonPhoneProperty);
            NSNumber * mobileNumber = (__bridge NSNumber *)ABMultiValueCopyValueAtIndex(multi, 0);
            
            ABMultiValueRef addresses = ABRecordCopyValue(contactPerson, kABPersonAddressProperty);
            for (CFIndex j = 0; j < ABMultiValueGetCount(addresses); j++){
                CFDictionaryRef dict = ABMultiValueCopyValueAtIndex(addresses, j);
                if (j == 0) {
                    street = [(NSString *)CFDictionaryGetValue(dict, kABPersonAddressStreetKey) copy];
                    city = [(NSString *)CFDictionaryGetValue(dict, kABPersonAddressCityKey) copy];
                    state = [(NSString *)CFDictionaryGetValue(dict, kABPersonAddressStateKey) copy];
                    zipCode = [(NSString *)CFDictionaryGetValue(dict, kABPersonAddressZIPKey) copy];
                    country = [(NSString *)CFDictionaryGetValue(dict, kABPersonAddressCountryKey) copy];
                } else if (j == 1) {
                    streetWork = [(NSString *)CFDictionaryGetValue(dict, kABPersonAddressStreetKey) copy];
                    cityWork = [(NSString *)CFDictionaryGetValue(dict, kABPersonAddressCityKey) copy];
                    stateWork = [(NSString *)CFDictionaryGetValue(dict, kABPersonAddressStateKey) copy];
                    zipCodeWork = [(NSString *)CFDictionaryGetValue(dict, kABPersonAddressZIPKey) copy];
                    countryWork = [(NSString *)CFDictionaryGetValue(dict, kABPersonAddressCountryKey) copy];
                } else if (j == 2) {
                    streetOther = [(NSString *)CFDictionaryGetValue(dict, kABPersonAddressStreetKey) copy];
                    cityOther = [(NSString *)CFDictionaryGetValue(dict, kABPersonAddressCityKey) copy];
                    stateOther = [(NSString *)CFDictionaryGetValue(dict, kABPersonAddressStateKey) copy];
                    zipCodeOther = [(NSString *)CFDictionaryGetValue(dict, kABPersonAddressZIPKey) copy];
                    countryOther = [(NSString *)CFDictionaryGetValue(dict, kABPersonAddressCountryKey) copy];
                }
            }
            
            if(lastName.length == 0) {
                lastName = @"";
                fullName = [NSString stringWithFormat:@"%@ %@", firstName, lastName];
            } else {
                fullName = [NSString stringWithFormat:@"%@ %@", firstName, lastName];
            }
            
            if(city.length == 0) {
                city = @"";
            } else if (state.length == 0) {
                state = @"";
            } else if (country.length == 0) {
                country = @"";
            } else if (zipCode.length == 0) {
                zipCode = @"";
            }
            
            obPerson.strFirstName = firstName;
            obPerson.strLastName = lastName;
            obPerson.strFullName = fullName;
            
            obPerson.strStreet = street;
            obPerson.strCity = city;
            obPerson.strState = state;
            obPerson.strCountry = country;
            obPerson.strZipCode = zipCode;
            
            obPerson.strStreetWork = streetWork;
            obPerson.strCityWork = cityWork;
            obPerson.strStateWork = stateWork;
            obPerson.strCountryWork = countryWork;
            obPerson.strZipCodeWork = zipCodeWork;
            
            obPerson.strStreetOther = streetOther;
            obPerson.strCityOther = cityOther;
            obPerson.strStateOther = stateOther;
            obPerson.strCountryOther = countryOther;
            obPerson.strZipCodeOther = zipCodeOther;
            
            obPerson.kABPersonFirstNameProperty = ClonekABPersonFirstNameProperty;
            obPerson.kABPersonLastNameProperty = ClonekABPersonLastNameProperty;
            obPerson.kABPersonMiddleNameProperty = ClonekABPersonMiddleNameProperty;
            obPerson.kABPersonPrefixProperty = ClonekABPersonPrefixProperty;
            obPerson.kABPersonSuffixProperty = ClonekABPersonSuffixProperty;
            obPerson.kABPersonNicknameProperty = ClonekABPersonNicknameProperty;
            obPerson.kABPersonFirstNamePhoneticProperty = ClonekABPersonFirstNamePhoneticProperty;
            obPerson.kABPersonLastNamePhoneticProperty = ClonekABPersonLastNamePhoneticProperty;
            obPerson.kABPersonMiddleNamePhoneticProperty = ClonekABPersonMiddleNamePhoneticProperty;
            obPerson.kABPersonOrganizationProperty = ClonekABPersonOrganizationProperty;
            obPerson.kABPersonJobTitleProperty = ClonekABPersonJobTitleProperty;
            obPerson.kABPersonDepartmentProperty = ClonekABPersonDepartmentProperty;
            obPerson.kABPersonEmailProperty = ClonekABPersonEmailProperty;
            obPerson.kABPersonBirthdayProperty = ClonekABPersonBirthdayProperty;
            obPerson.kABPersonNoteProperty = ClonekABPersonNoteProperty;
            obPerson.kABPersonCreationDateProperty = ClonekABPersonCreationDateProperty;
            obPerson.kABPersonModificationDateProperty = ClonekABPersonModificationDateProperty;
            
            obPerson.flageShowOperation = NO;
            obPerson.strPhoneNumber =[NSString stringWithFormat:@"%@",mobileNumber];
            NSLog(@"obPerson--> %@",obPerson);
            
            ABMultiValueRef emails = ABRecordCopyValue(contactPerson, kABPersonEmailProperty);
            
            NSUInteger j = 0;
            for (j = 0; j < ABMultiValueGetCount(emails); j++) {
                NSString *email = (__bridge_transfer NSString *)ABMultiValueCopyValueAtIndex(emails, j);
                if (j == 0) {
                    obPerson.strHomeEmail = email;
                } else if (j==1) {
                    obPerson.strWorkEmail = email;
                }
            }
            
            [arrContactList addObject:obPerson];
            [arrContactListBackUp addObject:obPerson];
        
            [self.view reloadInputViews];
        }
    }
    
    /*
    if ([[SettingData sharedInstance] flageSortPeopleContact] && arrContactList.count > 0) {
        NSMutableArray * arrName = [[NSMutableArray alloc] init];
        for (Person * obPerson in arrContactList) {
            [arrName addObject:obPerson.strFullName];
        }
        
        NSArray *sortedArray = [arrName sortedArrayUsingFunction:finderSortWithLocale context:(__bridge void *)([NSLocale currentLocale])];
        NSMutableArray * arrFromContactNew = [[NSMutableArray alloc] init];
        for (int i = 0; i < [sortedArray count]; i++) {
            for (Person * obPerson in arrContactList) {
                if ([[sortedArray objectAtIndex:i] isEqualToString:obPerson.strFullName]) {
                    [arrFromContactNew addObject:obPerson];
                    break;
                }
            }
        }
     
        arrContactList = [NSMutableArray arrayWithArray:arrFromContactNew];
        arrContactListBackUp = [NSMutableArray arrayWithArray:arrFromContactNew];
    }
     */
    
    [tbl_contacts reloadData];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

- (IBAction)btnBack:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}
@end
