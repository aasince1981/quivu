//
//  ViewOfferScreen.h
//  Reloved
//
//  Created by Kamal on 28/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ViewOfferCell.h"

@interface ViewOfferScreen : UIViewController <UITableViewDataSource,UITableViewDelegate,CellFunctionBtnViewOffer>
{
    
    IBOutlet UILabel *lblHeader;
    IBOutlet UITableView *tbl_ViewOffer;
    IBOutlet UIImageView *imgProductImage;
    IBOutlet UILabel *lblProductName;
    IBOutlet UILabel *lblListedPrice;
    IBOutlet UILabel *lblProductPrice;
    
    NSMutableArray * arrOfferList;
}
@property (strong, nonatomic) NSMutableDictionary * DictProductDetails;

- (IBAction)BtnReferesh:(id)sender;
- (IBAction)BtnBack:(id)sender;

@end
