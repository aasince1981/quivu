//
//  ViewOfferCell.h
//  Reloved
//
//  Created by Kamal on 28/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol CellFunctionBtnViewOffer <NSObject>
- (void) BtnViewOfferAction: (int) indexValue;
@end

@interface ViewOfferCell : UITableViewCell

@property NSInteger getIndex;
@property (assign ,nonatomic) id<CellFunctionBtnViewOffer> delegate;
@property (strong, nonatomic) IBOutlet UILabel *lblProductPrice;
@property (strong, nonatomic) IBOutlet UILabel *lblUserName;
@property (strong, nonatomic) IBOutlet UILabel *lblTimeDuration;
@property (strong, nonatomic) IBOutlet UIImageView *imgUserImage;

- (IBAction)btnViewOffer:(id)sender;
@end
