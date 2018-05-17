; SMSQ Serial (SerParPrt) IO Driver versions

	section version

	xdef	spp_vers

; V2.00   First version.
;
; V2.01   Dynamic output buffering added.
;
; V2.02   Set and read headers added to V2.01.
;
; V2.03   Output interrupt routine simplified.
;
; V2.04   Zero length translated strings now OK.
;
; V2.05   Static buffer with translate now OK.
;
; V2.06   XON/XOFF added, Carrier Detect Timeout added
;	  Test Byte corrected
;
; V2.07   Auto CR added
;
; V2.08   Null passed (disappeared in V2.07)
;
; V2.09   MC680x0 compatible
;
; V2.10   Handshake still works if SER_ROOM is 0, but it may still loose chars.
;	  SER_BUFF n,x set SER_ROOM to x/4+1
;
; V2.11   CDEOF has no effect until carrier is asserted for the first time.
;
; V2.12   CDEOF really has no effect until carrier is asserted for the first
;		time.
;
; V2.13   Input buffer pointers corrected (should have no effect).
;
; V2.14   Atari SCC and TTMFP supported.
;
; V2.15   Atari SCC channel A (SER4) does not now check receive buffer bit.
;
; V2.16   Generalised version.
;
; V2.17   More generalised version combines SER and PAR

spp_vers equ	'2.17'
	end
