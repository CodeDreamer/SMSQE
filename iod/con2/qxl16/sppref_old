; 16 bit sprite preference table    V2.02   1999  Tony Tebby
;
; 2002-11-12  2.01  Added mode 64 definition (MK)
; 2002-12-15  2.02  Added more mode definitions (JG)

	section driver

	xdef	pt_sppref     ; 14 sprite preference table
	xdef	pt_spnative

; The size and order of the table must comply with the definition
; in iod_con2_16_spcch_asm!
;
; Currently the order is: native, 24bit, mode 33, mode 32, 8bit, 8bit pal
; , 4 bit, 4 bit pal, 2 bit (ql 4 colour like), 2 bit pal,
; , 4 colour ql, 1 bit, 1 bit pal, 8 colour ql
;

pt_spnative
pt_sppref dc.w	 $0220,$0240,$0221,$0220,$0210,$021f
	  dc.w	 $0208,$020f,$0204,$0207
	  dc.w	 $0100,$0200,$0203,$0101

	end
