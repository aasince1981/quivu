//
//  Person.h
//  AddressBookDemo
//
//  Created by Arthur Knopper on 24/10/12.
//  Copyright (c) 2012 iOSCreator. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Person : NSObject

@property (nonatomic, strong) NSString *strFirstName;
@property (nonatomic, strong) NSString *strLastName;
@property (nonatomic, strong) NSString *strFullName;
@property (nonatomic, strong) NSString *strHomeEmail;
@property (nonatomic, strong) NSString *strWorkEmail;
@property (nonatomic, strong) NSString *strPhoneNumber;

@property (nonatomic, strong) NSString *strStreet;
@property (nonatomic, strong) NSString *strState;
@property (nonatomic, strong) NSString *strCity;
@property (nonatomic, strong) NSString *strCountry;
@property (nonatomic, strong) NSString *strAddress;
@property (nonatomic, strong) NSString *strZipCode;

@property (nonatomic, strong) NSString *strStreetWork;
@property (nonatomic, strong) NSString *strStateWork;
@property (nonatomic, strong) NSString *strCityWork;
@property (nonatomic, strong) NSString *strCountryWork;
@property (nonatomic, strong) NSString *strAddressWork;
@property (nonatomic, strong) NSString *strZipCodeWork;

@property (nonatomic, strong) NSString *strStreetOther;
@property (nonatomic, strong) NSString *strStateOther;
@property (nonatomic, strong) NSString *strCityOther;
@property (nonatomic, strong) NSString *strCountryOther;
@property (nonatomic, strong) NSString *strAddressOther;
@property (nonatomic, strong) NSString *strZipCodeOther;

@property BOOL flageShowOperation;
@property BOOL flageNewOperation;

@property (nonatomic, strong) NSString *kABPersonFirstNameProperty;
@property (nonatomic, strong) NSString *kABPersonLastNameProperty;
@property (nonatomic, strong) NSString *kABPersonMiddleNameProperty;
@property (nonatomic, strong) NSString *kABPersonPrefixProperty;
@property (nonatomic, strong) NSString *kABPersonSuffixProperty;
@property (nonatomic, strong) NSString *kABPersonNicknameProperty;
@property (nonatomic, strong) NSString *kABPersonFirstNamePhoneticProperty;
@property (nonatomic, strong) NSString *kABPersonLastNamePhoneticProperty;
@property (nonatomic, strong) NSString *kABPersonMiddleNamePhoneticProperty;
@property (nonatomic, strong) NSString *kABPersonOrganizationProperty;
@property (nonatomic, strong) NSString *kABPersonJobTitleProperty;
@property (nonatomic, strong) NSString *kABPersonDepartmentProperty;
@property (nonatomic, strong) NSString *kABPersonEmailProperty;
@property (nonatomic, strong) NSString *kABPersonBirthdayProperty;
@property (nonatomic, strong) NSString *kABPersonNoteProperty;
@property (nonatomic, strong) NSString *kABPersonCreationDateProperty;
@property (nonatomic, strong) NSString *kABPersonModificationDateProperty;

- (id)initWith:(Person *) ob;

@end
