//
//  NSString.h
//  NetworkRequestProject
//
//  Created by Ideal IT Techno on 24/05/14.
//  Copyright (c) 2014 Technology. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface NSString (custom)
{
    
}

- (NSData *) base64DataFromString:(NSString *) string;
- (NSString *) base64forData:(NSData *) theData;
- (NSString *) imageNameFromUrl;

@end
