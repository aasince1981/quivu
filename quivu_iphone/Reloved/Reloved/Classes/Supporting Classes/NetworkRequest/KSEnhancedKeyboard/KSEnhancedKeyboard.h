//
//  KSEnhancedKeyboard.h
//  CustomKeyboardForm
//
//  Created by Krzysztof Satola on 10.12.2012.
//  Copyright (c) 2012 API-SOFT. All rights reserved.
//

#import <Foundation/Foundation.h>

// ====================================================================
@protocol KSEnhancedKeyboardDelegate

- (void)nextDidTouchDown;
- (void)previousDidTouchDown;
- (void)doneDidTouchDown;

@end

// ====================================================================
@interface KSEnhancedKeyboard : NSObject

@property (nonatomic, strong) id <KSEnhancedKeyboardDelegate> delegate;

- (UIToolbar *)getToolbarWithPrevEnabled:(BOOL)prevEnabled NextEnabled:(BOOL)nextEnabled DoneEnabled:(BOOL)doneEnabled;

@end
