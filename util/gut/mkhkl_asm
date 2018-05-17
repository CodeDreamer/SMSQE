; Make a list of Executable hotkeys     V0.00   1989  Tony Tebby   QJUMP

        section gut

        include 'dev8_keys_err'
        include 'dev8_keys_hkl'
        include 'dev8_ee_hk_data'
        include 'dev8_ee_hk_vector'

        xdef    gu_mkhkl
        xdef    gu_mkxhl
        xdef    gu_mkphl

        xref    gu_mklis
        xref    gu_hkuse
        xref    gu_hkfre
        xref    cv_hktyp
;+++
; Make a list of all Program Hotkeys (those that start a program).
;
;       Registers:
;               Entry                           Exit
;       D0                                      0 or error code
;       D1                                      number of entries
;       A1                                      pointer to list
;---
gu_mkphl
;                    .   :   .   |   .   :   .   |
        move.l  #%00111100111100000000000000000000,d0 ; execute/load Hotkeys
        bra.s   ghl_do

;+++
; Make a list of all Executable Hotkeys (those that execute Things).
;
;       Registers:
;               Entry                           Exit
;       D0                                      0 or error code
;       D1                                      number of entries
;       A1                                      pointer to list
;---
gu_mkxhl
;                    .   :   .   |   .   :   .   |
        move.l  #%00001100001100000000000000000000,d0 ; execute Hotkeys
        bra.s   ghl_do

;+++
; Make a list of all Hotkeys (except last line and buffer).
;
;       Registers:
;               Entry                           Exit
;       D0                                      0 or error code
;       D1                                      number of entries
;       A1                                      pointer to list
;---
gu_mkhkl
;                    .   :   .   |   .   :   .   |
        move.l  #%11111111111111111100000000000000,d0 ; not last line / buffer
ghl_do
ghl.reg reg     d2/a0/a3
        movem.l ghl.reg,-(sp)
        move.l  d0,d2                    ; keep mask
        jsr     gu_hkuse                 ; get the linkage block
        bne.s   ghl_exit                 ; ... oops
        move.w  #-257,d1
        moveq   #hkl.elen,d0             ; entry length
        lea     ghl_info,a0              ; how to fill in an entry
        jsr     gu_mklis                 ; make a list
        jsr     gu_hkfre
ghl_exit
        movem.l (sp)+,ghl.reg
        rts
;+++
; Fill in next Hotkey.  Called from GU_MKLIS at the request of GU_MKHKL etc.
;
;       Registers:
;               Entry                           Exit
;       D0                                      error code
;       D1      key to test                     next key to test
;       D2      mask of Hotkey types+16         preserved
;       A1      entry to fill in                preserved
;       A3      hotkey linkage block            preserved
;---
ghl_info
ghi.reg reg     d2/d3/d4/a0/a1/a4
        movem.l ghi.reg,-(sp)
        move.l  a1,a4                   ; keep entry safe
        move.l  d2,d3                   ; and mask
        move.w  d1,d4                   ; and key
ghi_next
        addq.w  #1,d4
        beq.s   ghi_eof                 ; ... yes
        lea     hkl_name(a4),a1
        move.w  #1,(a1)                 ; one character long
        move.b  d4,2(a1)                ; make string of key
        move.l  hk.fitem(a3),a0
        jsr     (a0)                    ; and find corresponding hotkey item
        bne.s   ghi_next                ; ... none, try next
        cmp.b   d1,d4                   ; same case?
        bne.s   ghi_next                ; ... no, try next

        move.w  hki_type(a1),d1         ; type
        moveq   #$10,d0
        add.w   d1,d0
        btst    d0,d3                   ; acceptable type?
        beq.s   ghi_next                ; ... no

        move.b  d4,hkl_key(a4)          ; set key
        move.w  d1,hkl_type(a4)         ; and type

        addq.l  #hki_name,a1            ; point to hotkey's name
        moveq   #hkl.maxn-7,d0          ; name can be this long
        move.w  (a1)+,d3                ; get actual length
        cmp.w   d0,d3                   ; too long?
        ble.s   ghi_cpch                ; no
        move.w  d0,d3                   ; yes, use max length
ghi_cpch
        move.l  d3,d0
        addq.l  #7,d0                   ; length of key+type+name
        lea     hkl_name(a4),a0         ; point to name in entry
        move.w  d0,(a0)+                ; set length
        move.b  d4,(a0)+                ; key
        move.b  #' ',(a0)+
        lsr.w   #1,d1
        lsl.w   #2,d1                   ; index type table
        lea     cv_hktyp,a4
        move.l  (a4,d1.w),(a0)+
        move.b  #' ',(a0)+
        bra.s   ghi_cpce
ghi_cpcl
        move.b  (a1)+,(a0)+             ; copy characters of name
ghi_cpce
        dbra    d3,ghi_cpcl

        move.w  d4,d1                   ; next key
        moveq   #0,d0                   ; no errors
ghi_exit
        movem.l (sp)+,ghi.reg
        rts
ghi_eof
        moveq   #err.eof,d0
        bra.s   ghi_exit
        end
