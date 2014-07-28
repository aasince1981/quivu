//
//  Person.m
//  AddressBookDemo
//
//  Created by Arthur Knopper on 24/10/12.
//  Copyright (c) 2012 iOSCreator. All rights reserved.
//

#import "Person.h"

@implementation Person
@synthesize strFirstName, flageNewOperation, strFullName, strHomeEmail, strLastName, strPhoneNumber, strWorkEmail, flageShowOperation;
@synthesize strAddress, strCity, strCountry, strState, strStreet, strZipCode, strAddressWork, strCityWork, strCountryWork, strStateWork, strStreetWork, strZipCodeWork, strAddressOther, strCityOther, strCountryOther, strStateOther, strStreetOther, strZipCodeOther;

@synthesize kABPersonFirstNameProperty;
@synthesize kABPersonLastNameProperty;
@synthesize kABPersonMiddleNameProperty;
@synthesize kABPersonPrefixProperty;
@synthesize kABPersonSuffixProperty;
@synthesize kABPersonNicknameProperty;
@synthesize kABPersonFirstNamePhoneticProperty;
@synthesize kABPersonLastNamePhoneticProperty;
@synthesize kABPersonMiddleNamePhoneticProperty;
@synthesize kABPersonOrganizationProperty;
@synthesize kABPersonJobTitleProperty;
@synthesize kABPersonDepartmentProperty;
@synthesize kABPersonEmailProperty;
@synthesize kABPersonBirthdayProperty;
@synthesize kABPersonNoteProperty;
@synthesize kABPersonCreationDateProperty;
@synthesize kABPersonModificationDateProperty;

- (id)init {
    self = [super init];
    if (self) { }
    return self;
}

- (id)initWith:(Person *) ob {
    self.strFirstName = ob.strFirstName;
    self.strLastName = ob.strLastName;
    self.strFullName = ob.strFullName;
    self.strHomeEmail = ob.strHomeEmail;
    self.strWorkEmail = ob.strWorkEmail;
    self.strPhoneNumber = ob.strPhoneNumber;
    
    self.strStreet = ob.strStreet;
    self.strState = ob.strState;
    self.strCity = ob.strCity;
    self.strCountry = ob.strCountry;
    self.strAddress = ob.strAddress;
    self.strZipCode = ob.strZipCode;
    
    self.strStreetWork = ob.strStreetWork;
    self.strStateWork = ob.strStateWork;
    self.strCityWork = ob.strCityWork;
    self.strCountryWork = ob.strCountryWork;
    self.strAddressWork = ob.strAddressWork;
    self.strZipCodeWork = ob.strZipCodeWork;
    
    self.strStreetOther = ob.strStreetOther;
    self.strStateOther = ob.strStateOther;
    self.strCityOther = ob.strCityOther;
    self.strCountryOther = ob.strCountryOther;
    self.strAddressOther = ob.strAddressOther;
    self.strZipCodeOther = ob.strZipCodeOther;
    
    self.flageShowOperation = ob.flageShowOperation;
    self.flageNewOperation = ob.flageNewOperation;
    
    
    self.kABPersonFirstNameProperty = kABPersonFirstNameProperty;
    self.kABPersonLastNameProperty = kABPersonLastNameProperty;
    self.kABPersonMiddleNameProperty = kABPersonMiddleNameProperty;
    self.kABPersonPrefixProperty = kABPersonPrefixProperty;
    self.kABPersonSuffixProperty = kABPersonSuffixProperty;
    self.kABPersonNicknameProperty = kABPersonNicknameProperty;
    self.kABPersonFirstNamePhoneticProperty = kABPersonFirstNamePhoneticProperty;
    self.kABPersonLastNamePhoneticProperty = kABPersonLastNamePhoneticProperty;
    self.kABPersonMiddleNamePhoneticProperty = kABPersonMiddleNamePhoneticProperty;
    self.kABPersonOrganizationProperty = kABPersonOrganizationProperty;
    self.kABPersonJobTitleProperty = kABPersonJobTitleProperty;
    self.kABPersonDepartmentProperty = kABPersonDepartmentProperty;
    self.kABPersonEmailProperty = kABPersonEmailProperty;
    self.kABPersonBirthdayProperty = kABPersonBirthdayProperty;
    self.kABPersonNoteProperty = kABPersonNoteProperty;
    self.kABPersonCreationDateProperty = kABPersonCreationDateProperty;
    self.kABPersonModificationDateProperty = kABPersonModificationDateProperty;
    
    return self;
}

@end
