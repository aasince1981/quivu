//
//  NearByViewController.m
//  Rideout
//
//  Created by Ideal IT Techno on 20/11/13.
//  Copyright (c) 2013 Ideal It Technology. All rights reserved.
//

#import "NearByViewController.h"
#import "CustomCell1.h"

@interface NearByViewController ()

@end

@implementation NearByViewController
@synthesize whichPlaces;
@synthesize boolFromDriverLocation;

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
    
    arrNearByItems = [NSMutableArray arrayWithObjects:
        @"accounting",
        @"airport",
        @"amusement_park",
        @"aquarium",
        @"art_gallery",
        @"atm",
        @"bakery",
        @"bank",
        @"bar",
        @"beauty_salon",
        @"bicycle_store",
        @"book_store",
        @"bowling_alley",
        @"bus_station",
        @"cafe",
        @"campground",
        @"car_dealer",
        @"car_rental",
        @"car_repair",
        @"car_wash",
        @"casino",
        @"cemetery",
        @"church",
        @"city_hall",
        @"clothing_store",
        @"convenience_store",
        @"courthouse",
        @"dentist",
        @"department_store",
        @"doctor",
        @"electrician",
        @"electronics_store",
        @"embassy",
        @"establishment",
        @"finance",
        @"fire_station",
        @"florist",
        @"food",
        @"funeral_home",
        @"furniture_store",
        @"gas_station",
        @"general_contractor",
        @"grocery_or_supermarket",
        @"gym",
        @"hair_care",
        @"hardware_store",
        @"health",
        @"hindu_temple",
        @"home_goods_store",
        @"hospital",
        @"insurance_agency",
        @"jewelry_store",
        @"laundry",
        @"lawyer",
        @"library",
        @"liquor_store",
        @"local_government_office",
        @"locksmith",
        @"lodging",
        @"meal_delivery",
        @"meal_takeaway",
        @"mosque",
        @"movie_rental",
        @"movie_theater",
        @"moving_company",
        @"museum",
        @"night_club",
        @"painter",
        @"park",
        @"parking",
        @"pet_store",
        @"pharmacy",
        @"physiotherapist",
        @"place_of_worship",
        @"plumber",
        @"police",
        @"post_office",
        @"real_estate_agency",
        @"restaurant",
        @"roofing_contractor",
        @"rv_park",
        @"school",
        @"shoe_store",
        @"shopping_mall",
        @"spa",
        @"stadium",
        @"storage",
        @"store",
        @"subway_station",
        @"synagogue",
        @"taxi_stand",
        @"train_station",
        @"travel_agency",
        @"university",
        @"veterinary_care",
        @"zoo", nil];
    
    arrNearByPlace = [[NSMutableArray alloc] init];
    arrSearchClone = [[NSMutableArray alloc] init];
    
    tbl_NearByPlace.delegate = self;
    tbl_NearByPlace.dataSource = self;
    
    tf_Search.delegate = self;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

- (void)viewWillAppear:(BOOL)animated
{
    KAppDelegate.MyVC = self;
    [self getDataOfMap];
}

#pragma mark - tableview dataSource and Delegate methods
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return arrNearByPlace.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    static NSString * strCell = @"strCell";
    CustomCell1 *cell = (CustomCell1 *)[tableView dequeueReusableCellWithIdentifier:strCell];
    if(cell == nil) {
        NSArray *nib = [[NSBundle mainBundle]loadNibNamed:@"CustomCell1" owner:self options:nil];
        cell=(CustomCell1*)[nib objectAtIndex:0];
    }
    
    NSDictionary * dict = [arrNearByPlace objectAtIndex:indexPath.row];
    cell.name.text = [dict objectForKey:@"name"];
    [cell.Image_view GetNSetUIImage:[dict objectForKey:@"icon"] DefaultImage:@""];
    
    //[getNetworkConnection getImage:[dict objectForKey:@"icon"] defaultImage:@""];
    
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    return cell;
}

- (void) tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    NSLog(@"arrNearByPlace--> %@",[[arrNearByPlace objectAtIndex:indexPath.row] objectForKey:@"name"]);
    NSDictionary * dict = [arrNearByPlace objectAtIndex:indexPath.row];
    KAppDelegate.NearByPlace = [dict objectForKey:@"name"];
    [self.navigationController popViewControllerAnimated:YES];
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return 47.0f;
}

// here we get data on map..................
- (void) getDataOfMap {
    CLLocationManager * lm = [AppDelegate getLoactionManager];
    [lm startUpdatingLocation];
    
    if (lm.location.coordinate.latitude != 0.0 && lm.location.coordinate.longitude != 0.0) {
        NSString * name = @"";
        
        if (whichPlaces == 1) {
            for (int i = 0; i < [arrNearByItems count]; i++) {
                if (name.length == 0) {
                    name = [NSString stringWithFormat:@"%@", [arrNearByItems objectAtIndex:i]];
                } else {
                    name = [NSString stringWithFormat:@"%@|%@", name, [arrNearByItems objectAtIndex:i]];
                }
            }
        } else if (whichPlaces == 2) {
            name = @"airport";
        } else if (whichPlaces == 3) {
            name = @"locality";
        }
        
        if([obNet InternetStatus:YES]) {
            NSString * url = [NSString stringWithFormat:@"https://maps.googleapis.com/maps/api/place/search/json?location=%@&radius=%@&types=%@&sensor=%@&key=%@", [NSString stringWithFormat:@"%f,%f", lm.location.coordinate.latitude, lm.location.coordinate.longitude], @"10000", name, @"false", @"AIzaSyCXa5R4ZyUXmQjVhq_J82mAQyQIdQQKaxE"];
            
            NSLog(@"%@", url);
            
            [obNet JSONFromWebServices:url Parameter:nil AI:YES PopUP:NO WithBlock:^(id json) {
                if(json != nil) {
                    if([[json objectForKey:@"status"] isEqualToString:@"OK"]) {
                        arrNearByPlace = [json objectForKey:@"results"];
                        arrSearchClone = arrNearByPlace;
                        [tbl_NearByPlace reloadData];
                    }
                }
            }];
        }
    } else {
        [obNet PopUpMSG:@"Please enable GPS and allow GPS access to app." Header:@""];
    }
}

- (IBAction)BtnSearchIcon:(id)sender {
    
    if (tf_Search.text.length > 0) {
        arrNearByPlace = [[NSMutableArray alloc] init];

        for (int i = 0; i < arrSearchClone.count; i++) {
            NSDictionary * ddd = [arrSearchClone objectAtIndex:i];
            if (([[ddd objectForKey:@"name"] rangeOfString:tf_Search.text options:NSCaseInsensitiveSearch].location != NSNotFound)|| ([[ddd objectForKey:@"icon"] rangeOfString:tf_Search.text options:NSCaseInsensitiveSearch].location != NSNotFound)) {
                [arrNearByPlace addObject:ddd];
            }
        }
    } else {
        arrNearByPlace = [NSMutableArray arrayWithArray:arrSearchClone];
    }
    
    [tbl_NearByPlace reloadData];
}

- (IBAction)BtnBack:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

- (BOOL)textFieldShouldReturn:(UITextField *) textField
{
    [textField resignFirstResponder];
    return YES;
}

@end
