//
//  ViewOfferCell.m
//  Reloved
//
//  Created by Kamal on 28/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import "ViewOfferCell.h"

@implementation ViewOfferCell
@synthesize lblProductPrice,lblTimeDuration,lblUserName,imgUserImage,getIndex,delegate;

- (void)awakeFromNib
{
    // Initialization code
    
    lblUserName.font = FONT_Lato_Bold(14.0f);
    lblProductPrice.font = FONT_Lato_Bold(16.0f);
    lblTimeDuration.font = FONT_Lato_Bold(12.0f);
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (IBAction)btnViewOffer:(id)sender {
    [delegate BtnViewOfferAction:getIndex];
}

@end
