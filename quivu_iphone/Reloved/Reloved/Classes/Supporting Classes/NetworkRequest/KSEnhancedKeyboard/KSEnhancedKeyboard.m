//
//  KSEnhancedKeyboard.m
//  CustomKeyboardForm
//
//  Created by Krzysztof Satola on 10.12.2012.
//  Copyright (c) 2012 API-SOFT. All rights reserved.
//

#import "KSEnhancedKeyboard.h"

// ====================================================================
@implementation KSEnhancedKeyboard

// --------------------------------------------------------------------
- (UIToolbar *)getToolbarWithPrevEnabled:(BOOL)prevEnabled NextEnabled:(BOOL)nextEnabled DoneEnabled:(BOOL)doneEnabled
{
    UIToolbar *toolbar = [[UIToolbar alloc] init];
    [toolbar setBarStyle:UIBarStyleBlackTranslucent];
    [toolbar sizeToFit];
    
    NSMutableArray *toolbarItems = [[NSMutableArray alloc] init];
    
    UISegmentedControl *leftItems = [[UISegmentedControl alloc] initWithItems:[NSArray arrayWithObjects:@"Previous", @"Next", nil]];
    leftItems.segmentedControlStyle = UISegmentedControlStyleBar;
    [leftItems setEnabled:prevEnabled forSegmentAtIndex:0];
    [leftItems setEnabled:nextEnabled forSegmentAtIndex:1];
    leftItems.momentary = YES; // do not preserve button's state
    [leftItems addTarget:self action:@selector(nextPrevHandlerDidChange:) forControlEvents:UIControlEventValueChanged];
    
    UIBarButtonItem *nextPrevControl = [[UIBarButtonItem alloc] initWithCustomView:leftItems];
    [toolbarItems addObject:nextPrevControl];
    
    UIBarButtonItem *flexSpace = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace target:self action:nil];
    [toolbarItems addObject:flexSpace];
    
    UIBarButtonItem *doneButton =[[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemDone target:self action:@selector(doneDidClick:)];
    [toolbarItems addObject:doneButton];
    
    toolbar.items = toolbarItems;
    
    return toolbar;
}

// --------------------------------------------------------------------
- (void)nextPrevHandlerDidChange:(id)sender
{
    if (!self.delegate) return;
    
    switch ([(UISegmentedControl *)sender selectedSegmentIndex])
    {
        case 0:
            //NSLog(@"Previous");
            [self.delegate previousDidTouchDown];
            break;
        case 1:
            //NSLog(@"Next");
            [self.delegate nextDidTouchDown];
            break;
        default:
            break;
    }
}

// --------------------------------------------------------------------
- (void)doneDidClick:(id)sender
{
    if (!self.delegate) return;
    
    //NSLog(@"Done");
    [self.delegate doneDidTouchDown];
}

@end
