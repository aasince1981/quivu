//
//  UIImageViewCustom.m
//  myFotocloset
//
//  Created by Ideal IT Techno on 19/05/14.
//  Copyright (c) 2014 Ideal It Technology. All rights reserved.
//

#import "UIImageView+Custom.h"

@implementation UIImageView (custom)

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
    }
    return self;
}

- (void) GetNSetUIImage:(NSString *) url DefaultImage:(NSString *) defaultImage
{
    if (url) {
        if (defaultImage) {
            [self performSelectorInBackground:@selector(GetNSetUIImage:) withObject:[NSArray arrayWithObjects:url, defaultImage, nil]];
        } else {
            [self performSelectorInBackground:@selector(GetNSetUIImage:) withObject:[NSArray arrayWithObjects:url, @"", nil]];
        }
    }
}

- (void) GetNSetUIImage:(NSArray *) arr
{
    if (arr.count == 2) {
        NSString * url = arr[0];
        NSString * defaultImage = arr[1];
        
        NSString * imgFileName = [NSString stringWithFormat:@"%@", [url imageNameFromUrl]];
        NSArray * paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
        NSString * documentsDirec = [paths objectAtIndex:0];
        NSString * savedPath = [documentsDirec stringByAppendingPathComponent:imgFileName];
        
        UIImage * image = [UIImage imageWithContentsOfFile:savedPath];
        
        if (image) {
            [self performSelectorInBackground:@selector(setImageMy:) withObject:image];
        } else {
            NSURLRequest * request = [NSURLRequest requestWithURL:[NSURL URLWithString:url]];
            [NSURLConnection sendAsynchronousRequest:request
                                               queue:[NSOperationQueue mainQueue]
                                   completionHandler:^(NSURLResponse * response, NSData * data, NSError * error)
             {
                 UIImage * image = nil;
                 
                 if (data != nil) {
                     image = [UIImage imageWithData:data];
                     
                     [self performSelectorInBackground:@selector(setImageMy:) withObject:image];
                     
                     NSArray * paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
                     NSString * basePath = ([paths count] > 0) ? [paths objectAtIndex:0] : nil;
                     
                     NSString * filePath = [basePath stringByAppendingPathComponent:[NSString stringWithFormat:@"%@", [url imageNameFromUrl]]];
                     [data writeToFile:filePath atomically:YES];
                 } else {
                     image = [UIImage imageNamed:defaultImage];
                     
                     NSArray *directoryContents =  [[NSFileManager defaultManager] contentsOfDirectoryAtPath:[NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES)lastObject] error:NULL];
                     
                     if([directoryContents count] > 0) {
                         for (NSString *path in directoryContents) {
                             NSString *fullPath = [[NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES)lastObject] stringByAppendingPathComponent:path];
                             
                             NSRange r =[fullPath rangeOfString:[url imageNameFromUrl]];
                             if (r.location != NSNotFound || r.length == [[url imageNameFromUrl] length]) {
                                 [[NSFileManager defaultManager] removeItemAtPath:fullPath error:nil];
                             }
                         }
                     }
                 }
             }];
        }
    }
}

- (void) setImageMy:(UIImage *)image
{
    [self setImage:image];
}

@end
