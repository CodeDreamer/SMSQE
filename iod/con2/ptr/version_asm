* Version number/update record		v0.00   Feb 1988  J.R.Oakley  QJUMP
*
	section version
*
	xdef	pt_vers
	xdef	pt.vers
*
	dc.w	nmend-*-2,'Pointer Interface  V'
*
*	 v1.06.
*	 Key debounce improved. 		 (Extension)
*
*
*	 v1.07.
*	 First internal mouse version.		 (Extension)
*	 Closing last window in particular	 (Extension)
*	     mode now restores all windows
*	     in other mode.
*
*
*	 v1.08.
*	 Avoids problems with closing unused	 Never open a console
*	     consoles (It used to be able to	 without using it.
*	     lose the keyboard queue.)
*	 Improvements to screen restoration on	 (Extension)
*	     window close.
*
*
*	 v1.09
*	 Prevents channel 0 from being closed.	 (Extension)
*	 Mouse movement stuffs cursor		 (Extension)
*	     keystrokes into keyboard queue.
*	 SD.WDEF (WINDOW from SuperBASIC)	 Do explicit SD.POS after
*	     now resets cursor position.	 an SD.WDEF
*	 Multicolour patterns for blobs made	 Use only solid colours.
*	     usable.
*
*
*	 v1.10
*	 "Top" secondary is now the most	 Use SD.EXTOP to re-link
*	     recent one, not the first one.
*	 New TRAP IOP.FLIM, D0=$6C to find	 (Extension)
*	     permissible limits for window.
*	 New TRAPs IOP.SVPW/RSPW D0=$6D/6E	 (Extension)
*	     to save/restore part windows
*	 IOP.RPXL now implemented: new spec.	 (Extension)
*	     includes scanning
*	 FWIND now only detects managed
*	     secondaries of managed primaries
*	 IOP.OUTL can now move a secondary.	 Don't move secondaries.
*	 IOP.OUTL now deals with secondaries	 Re-define all
*	     that fall outside a re-defined	 secondaries when moving
*	     primary (now set to primary's hit   primary.
*	     area).
*	 Odd shadow widths evened up.		 Use only even width
*						 shadows.
*	 IOP.SPTR now only sets new position,	 No fix possible.
*	     so it works properly.
*	 Unmanaged secondaries now limited to	 Ensure secondaries are
*	     managed primary outline, not	 managed
*	     whole screen.
*	 IOP.PICK ignores lock. 		 Don't pick via a
*						 locked window.
*	 IOP.PICK allows keyboard queue to be	 (Cosmetic)
*	     grabbed, so cursor appears OK
*	 Hitting DO mouse button in keyboard	 (Extension)
*	     window stuffs an ENTER.
*	 Both buttons on mouse stuffs one or	 (Extension)
*	     two character string.
*	 Dropping blobs under sprite in MODE 8	 Don't drop blobs under
*	     fixed.				 sprite.
*	 Dynamic sprites implemented.		 (Extension)
*	 Pattern outside sprite mask is now	 (Extension)
*	     XORed into screen, not ORed.
*	 Extending an unmanaged locked		 Make primary managed
*	     primary's outline by opening a      or don't do it.
*	     larger secondary now works.
*
*
*	 v1.11
*
*
*	 v1.12
*	 First PTR_GEN.
*
*
*	 v1.13
*	 Move window on odd pixel boundary/	 Round to even boundary
*	     odd width now permitted (MODE 4).	 and width.
*
*	 v1.14
*	 ESC while doing special RPTR now gets
*	     through (got lost in vv1.xx-1.13).
*
*	 v1.15
*	 FWIND gets X size of sub-window	 Check returned pointer X
*	     correct, it was one too big	 coordinate size
*
*	 v1.16
*	 RPTR signals SCHED to make pointer
*	     visible.
*
*	 v1.17 first Atari ST Pointer Interface
*
*	 v1.18
*	 Patched to enable dropping of sprites	 Ensure Pointer is similar size
*	     and blobs which are larger than	 to object being dropped
*	     the pointer sprite.
*	 Save areas now owned by the same job	 (Cosmetic)
*	     as the channel, with null driver.
*	 Dummy CON is ROM CON, not current CON	 Fix for SpeedScreen
*
*	 v1.19
*	 CTRL F5 during MODE now works (!)	 I'm at a loss for words...
*	 RPIXL can now scan left/right for	 Check current pixel, set it,
*	    a given colour correctly		 scan for it, reset it
*	 Mode change between window open and	 Use window as soon as it's
*	     use is now OK.			 opened.
*	 Other dummy channels diverted via	 Fix for Lightning or other
*	     our linkage block, so MODE 	 modified console drivers
*	     doesn't spot them.
*	 Cursor status cleared before MODE	 (Cosmetic)
*	     window redraws.
*	 RPTR does not signal SCHED so much -	 (Cosmetic)
*	     see V1.16.
*
*	 v1.20
*	 All PICKs now move pointer to		 (Cosmetic)
*	     primary centre, not just CTRL C
*
*	 v1.21
*	 Window and border changes clear	 No fix possible
*	     scheduler flag
*
*	 v1.22
*	 Pick windowless JOB now OK on ATARI	 Do not pick windowless jobs
*
*	 v1.23
*	 CKEYON and CKEYOFF added to control	 Extension
*	     action of cursor keys
*	 WWA.KFLG added to window attributes
*	     for the same reason
*	 Type ahead enabled within a window	 Type slowly
*	 Third attempt at Thor 1 version	 Throw your Thor away
*	 Keypress suppressed on window change
*
*	 v1.24
*	 Thor 1 version allows Thor patch to be  Throw your Thor away
*	     used before loading Lightning
*
*	 v1.25
*	 Thor 1 version supports all three
*	     buttons on the Thor mouse
*	 Failure of DEL_DEFB introduced in	 Curse CST, Thor Intl etc.
*	     Thor mods to v1.23 fixed
*
*	v1.26 (internal)
*	Wake event generated when 'picking'	 Extension
*	    with DO button or if required
*	    in IOP.PICK trap (D2=K.WAKE)
*
*	v1.27 (internal)
*	Wake events improved			 Cosmetic
*	Keyboard queue of locked, busy or no
*	     window stripped
*
*	v1.28
*	Escape from window identify restored
*	    (problem in 1.27 only)
*
*	v1.29
*	CTRL C spurious wake removed
*	Problem with rapid "CTRL C"s removed
*	     (introduced in version 1.23)
*	HIT while moving restored (missing
*	     since V1.23)
*
*	v1.30
*	PICK to center of top secondary
*	Pointer movement slowed while disk
*	     etc busy
*
*	v1.31
*	Bad driver for save area corrected.
*	No wake-up on cursor key strokes
*
*	v1.32
*	Allocates enough room for a 64x48 pointer sprite.
*	Improvements to out of window keystrokes.
*
*	V1.33
*	Improved dragging. Pointer movement restored from v1.30
*	Checks for cursor overlap on RHS.
*
*	V1.34
*	Pointer movement slowed again.
*
*	V1.35
*	Cursor suppression algorithm improved.
*
*	V1.36
*	Corrected a fault in the V1.35 cursor suppression algorithm.
*	Pointer limiting introduced for dragging.
*
*	V1.37
*	Option to Freeze jobs on locking window.
*
*	V1.38
*	Close removes Fill buffer. Both ENTER keys on ST cause DO.
*
*	V1.39
*	IOP.RPXL removes pointer sprite.
*
*	V1.40
*	Higher RES mode supported
*
*	V1.41
*	Higher RES corrections
*
*	V1.42
*	Sprite / Blob dropping problems introduced in V1.40 fixed.
*
*	V1.43
*	Window area for non-well behaved windows can exceed 512x256.
*
*	V1.44
*	Some changes to sprite suppression / appearance
*
*	V1.45
*	More changes to sprite suppression / appearance
*
*	V1.46
*	IOW.SSIZ accepts -1,-1 for no change in size (size enquiry)
*
*	V1.47
*	Window Move $84 has invisible sprite
*
*	V1.48
*	Partial save / restore corrected for non-QL screen sizes.
*	Dragging restored (V1.45) even when pointer is being reset
*
*	V1.49
*	Sprite remove checks updated for wider screens.
*
*	V1.50
*	Partial save/restore updated for monochrome mode.
*
*	V1.51
*	sprite suppression / appearance restored to old style.
*
*	V1.52
*	Open CON (copyc) "out of memory" error recovery fixed.
*
*	V1.53
*	Initialisation works even if no RTC
*
*	V1.54
*	Modification of Atari polling routine
*
*	V1.55
*	IOP.RPXL corrected for non QL screens
*
*	V1.56
*	IOP.SVPW memory allocation modified - should have no effect
*
*	V1.57
*	Corrections to V1.56 for QDOS. MODE improvements
*
*	V1.58
*	Corrections to V1.55
*
*	V1.59
*	Cursor disabled on pointer operations (except rptr)
*
*	V1.60
*	Closing a secondary of an unlocked primary does not update the primary's
*	save area (avoiding overwrite)
*
*	V1.61
*	MODE change restored on QXL (lost in 1.57?)
*
*	V1.62
*	IOP.OUTL does not smash A1
*	PICK ignores badly behaved secondaries
*
*	V1.63
*	IOP.OUTL operation unchanged, but returns "bad parameter" if move
*	requested and the size has changed
*
*	V1.64
*	SD_WMODE is bits 3 to 1 only of SYS_QLMR (SV_MCSTA).
*
*	V1.65
*	Dummy queues re-organised
*
*	V1.66
*	A number of arbitrary changes (IOP.OUTL, IOP.SPTR etc.)
*	Changes to timings of hide / show etc.
*
*	V1.67
*	iop.svpw returns D1 = 0 and releases memory if fails.
*
*	V1.68
*	QIMI suppression config
*
*	V1.69
*	A certain amount of tidying up which should not make a difference
*
*	V1.70
*	pt..move not cleared by RPTR on out of window
*
*	V1.71
*	..... but sub-window number etc are cleared by RPTR on out of window
*
*	V1.72
*	accumulated set up updates including on edge of window events, job
*	events,  wake speed = 0.
*
*	V1.73
*	wallpaper colour is now drawn on the fly (MK)
*	16 bit shadow routine added (MK)
*
*	V1.74
*	added mode 64 (true colour in 32bit) for sprites (MK)
*	added even more modes (JG & MK)
*	fixed crashes and weird behaviour in low memory situations (MK)
*	added support for large sprites (MK)
*	no more MOVEP for Qx0 (FD)
*
*	V2.00
*	Bugfixes for some EE traps, keep A1 as per documentation (WL)
*	Added support for alpha blending and RLE compression of sprites (MK)
*	New con/ptr vectors concept introduced (MK)
*	Better handlng of system sprites, many more system sprites introduced (MK)
*	8 bit Aurora GD2 driver, also introduced in QPC (MK)
*
*	V2.01
*	Fixed wallpaper on mode change (MK)
*	More con vectors (WL)
*	More system sprites (MK)
*
*	V2.02
*	Support for sprites with no (=solid mask) (MK)
*	Fixed high colour shadow at the overlapping part (MK)
*
*	V2.03
*	Support for background update of covered windows (MK)
*	New con vector pv_bgctl (MK)
*	New SBASIC commands PE_BGOFF, PE_BGON (MK)
*	SBasic cannot be killed by changing the screen size (MK)
*	Screen is restored when an unbehaved window is changed/closed (MK)
*
*	V2.04
*	Default value for background I/O now configurable (MK)
*	New, optional, CTRL+C behaviour (configurable) (MK)
*
*	V2.05
*	PTR_GEN: A few fixes for QDOS (MK)
*
*	V2.06
*	PTR_GEN: Fixed mode changing/crashes on QDOS (MK)
*	PTR_GEN: Added native QemuLator support (MK)

pt.vers equ	'2.06'
pt_vers equ	pt.vers
	dc.l	pt.vers

nmend
	dc.b	$20,$0a
	end
