; Window manager Version     1988  Tony Tebby	QJUMP

	xdef	wm_vers
	xdef	wm.pver

;	V1.04	       WM.DRBDR added
;
;	V1.05	       Zero text pointers allowed, information blobs corrected
;
;	V1.06	       CHWIN returns size change. Initial pointer set rel hit
;		       area. Fixed window sizes accepted by SETUP. BREAK
;		       detected. Pending newline problems in information windows
;		       removed. Menu sub-window paper set before scrolling.
;
;	V1.07	       Lots of new routines. SSCLR and ARROW made regular fmt.
;
;	V1.08	       CHWIN fixed for secondaries/cursor keys.
;
;	V1.09	       Non-cleared info windows allowed.
;		       Vectors $48 to $74 added.
;
;	V1.10	       Setup correct number of columns > 3.
;		       Set pointer position correctly in odd position
;		       application sub-window.
;
;	V1.11	       Returns user defined message if no PTR.
;
;	V1.12	       Fractional scaling bug fixed.
;
;	V1.13	       WM.MSECT extended to accept cursor keys, SPACE and ENTER
;		       in arrow rows.
;		       DO/ENTER in arrow row of single item menu section made
;		       equivalent to HIT/SPACE.
;		       Cosmetic improvements to menu and current item handling.
;
;	V1.14	       WM.DROBJ updated to draw sprites 1 to 7 in the right
;		       place.
;
;	V1.15	       Sleep and wake event keystrokes added to WM.RPTR.
;		       Characters in the range $09 to $1f recognised in WM.RPTR.
;
;	V1.16	       Improved wake. Control codes less than 9 accepted.
;		       WM.RNAME, WM.ENAME return terminator in D1 as it should.
;
;	V1.17  (int)   Pan/scroll bars at last.
;
;	V1.18  (int)   Pan/scroll arrows made optional, bars tidied up.
;
;	V1.19  (int)   WM.SWAPP corrected for application windows >0.
;		       Improvements to out of window keystrokes.
;		       WM.CHWIN now allows cursor keys for pull down window
;			 moves - regardless of circumstances.
;
;	V1.20  (int)   Out of window wake accepted again (went in 1.19).
;
;	V1.21  (int)   DO anywhere in window accepted.
;
;	V1.22  (int)   Constant Spacing in menus.
;
;	V1.23  (int)   Repeated selection key handled.
;		       Dragging on pan/scroll bars implemented.
;
;	V1.24	       Improvements to FSIZE for windows variable in two dims.
;
;	V1.25	       Further improvements to pan/scroll bars.
;
;	V1.26	       WM.STLOB status set OK.
;		       WM.UPBAR added.
;
;	V1.27	       WM.SWLIT now sets cursor position using justification.
;		       WM.RNAME WN.ENAME start from cursor position.
;
;	V1.28	       Pan/scroll bars with no sections cleared (V1.26, V1.27)
;
;	V1.29	       Sub-window select keystroke (-1 in D2) re-introduced.
;
;	V1.30	       Sub-window control routine called only on move or hit.
;		       Window origin scaleable.
;		       DRBAR can draw full length bar (V1.26-V1.29).
;		       Event with no loose item accepted anywhere in window.
;
;	V1.31	       Underline nth character of text type -n.
;		       WM.MHIT returns D4=0 if action or control routine called.
;
;	V1.32	       DO item action routine called on DO in window
;
;	V1.33	       Text position set before character size set (prevents
;			  spurious scroll
;
;	V1.34	       Split cannot generate empty sections.
;
;	V1.35	       Character size only set if non-standard.
;		       Requires 1.46 Pointer.
;
;	V1.36	       WDRAW corrected so as not to smash d5/d6 (error in 1.35).
;
;	V1.37	       Scaling of menu spacing.
;		       Fixed menu spacing (first spacing negative) allowed
;			  in definition.
;
;	V1.38	       Minimum limit for window rounded up to 4 pixel boundary.
;
;	V1.39	       CHWIN does not smash D4 and D7 on move.
;
;	V1.40	       Underline permitted for text starting with spaces.
;
;	V1.41	       WM.RNAME WM.ENAME does not edit text longer than window.
;
;	V1.42	       Extended WM.RNAME WM.ENAME
;
;	V1.43	       Set window resets character size to 0,0
;
;	V1.44	       Pan and scroll bars corrected for border >1
;
;	V1.45	       V1.44 Corrected
;
;	V1.46	       CSIZE reset when no info text item
;
;	V1.47	       Corrects version 1.46
;
;	First SMSQ/E version
;	V1.48	       allows error messages longer than 40 characters
;
;	V1.49	       Attempts to remove 4096 item / row / column problems
;
;	V1.50	       WM.RPTRT introduced
;
;	V1.51	       and returns on job event
;
;	V1.52	       and checks explicitly for D3 reset by app sw hit
;
;	V1.53	       underscore checks improved
;
;	V1.54	       high colour definition introduced (early test version)
;
;	V1.55	       high colour changes finished (MK)
;		       introduced system palette (MK)
;		       wm.setsp, wm.getsp, wm.trap3 added (MK)
;		       rewrote most of the scroll/pan bar draw routines (MK)
;
;	V2.00	       wm.opw, wm.ssclr, wm.jbpal added (MK)
;		       Sprites may be drawn differently depending on the item
;			   status (WL + MK)
;
;	V2.01	       New window move routines & Config Block (WL + MK)
;		       Bugfix for object drawing & wman rptr routines (WL & MK)
;		       Fixed bug for stippled borders in WMAN (WL)
;
;	V2.02	       Fixed WM_BLOCK (GG + MK)
;
;	V2.03	       Fixed pointer save on new move operation (wl)
;
;	V2.04	       Fixed spurious outline when using outline move, on mouse button release
;
;	V2.05	       New vector wm_cpspr to copy a sprite definition (MK)
;		       Fixed error return of wm.jbpal (MK)
;		       Double 3d border type fixed (MK)
;
;	V2.06	       New window move with transparency (wl)
;
;	V2.07	       menu appsub wdw may have csizes for text objects (wl)
;
;	V2.08	       QDOS version optimized for space (MK)
;
;	V2.09	       indexes drawing added (AH)

wm_vers equ	'2.09'
wm.pver equ	'2.02'

	end
