//
//  NearByViewController.h
//  Rideout
//
//  Created by Ideal IT Techno on 20/11/13.
//  Copyright (c) 2013 Ideal It Technology. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface NearByViewController : UIViewController <UITableViewDataSource,UITableViewDelegate,UITextFieldDelegate>
{
    NSMutableArray * arrNearByPlace;
    
    IBOutlet UITableView *tbl_NearByPlace;
    NSMutableArray * arrNearByItems;
     NSMutableArray * arrSearchClone;
    
    IBOutlet UITextField *tf_Search;
}

@property BOOL boolFromDriverLocation;
@property int whichPlaces;

- (IBAction)BtnSearchIcon:(id)sender;
- (IBAction)BtnBack:(id)sender;
@end
