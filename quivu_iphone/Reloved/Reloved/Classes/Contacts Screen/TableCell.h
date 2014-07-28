//
//  TableCell.h
//  Digital Card
//
//  Created by ideal indore on 02/09/13.
//  Copyright (c) 2013 ideal indore. All rights reserved.
//

#import <UIKit/UIKit.h>

@class  PeopleViewController;

/*
@protocol ContactTableCellDelegate <NSObject>
- (void) CellBtnCall:(NSIndexPath *)indexPath;
- (void) CellBtnEditContact:(NSIndexPath *)indexPath;
- (void) CellBtnDeleteContact:(NSIndexPath *)indexPath;
- (void) CellBtnLocation:(NSIndexPath *)indexPath;
- (void) CellBtnEmail_HomeScreen:(NSIndexPath *)indexPath;
- (void) CellBtnSendText:(NSIndexPath *)indexPath;
- (void) CellBtnSendvCardEmail:(NSIndexPath *)indexPath;
- (void) CellBtnGetDirections:(NSIndexPath *)indexPath;
@end
 */

@interface TableCell : UITableViewCell

@property (strong, nonatomic) IBOutlet UILabel *lblName;
@property (strong, nonatomic) IBOutlet UILabel *lblNumber;

//@property (strong, nonatomic) id <ContactTableCellDelegate> delegate;

@end
