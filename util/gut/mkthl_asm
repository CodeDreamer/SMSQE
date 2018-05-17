; Make a list of Things     V0.01   1989  Tony Tebby   QJUMP

        section gut

        xdef    gu_mkthl
        xdef    gu_mkexl
        xdef    gu_mktjl

        xref    gu_mklis
        xref    gu_thjmp

        include 'dev8_keys_err'
        include 'dev8_keys_thg'
        include 'dev8_keys_k'
        include 'dev8_keys_qdos_sms'
        include 'dev8_keys_thl'

;+++
; Make a list of all Executable Things.
;
;       Registers:
;               Entry                           Exit
;       D0                                      0 or error code
;       D1                                      number of entries
;       A1                                      pointer to list
;       A3      pointer to unique character list
;---
gu_mkexl
        moveq   #tht.exec,d0             ; flag for executable required
        moveq   #-1,d1                   ; any users
        bra.s   gtl_do
;+++
; Make a list of all Things
;
;       Registers:
;               Entry                           Exit
;       D0                                      0 or error code
;       D1                                      number of entries
;       A1                                      pointer to list
;       A3      pointer to unique character list
;---
gu_mkthl
        moveq   #-1,d1                   ; any user
;+++
; Make a list of all Things used by job
;
;       Registers:
;               Entry                           Exit
;       D0                                      0 or error code
;       D1      Job ID (-1 any thing)           number of entries
;       A1                                      pointer to list
;       A3      pointer to unique character list
;---
gu_mktjl
        moveq   #0,d0                    ; anything goes

gtl_do
gtl.reg reg     d2/a0/a2/a3
        movem.l gtl.reg,-(sp)
        move.l  d0,d2                    ; keep selection key
        moveq   #thl.elen,d0             ; entry length
        lea     gtl_info,a0              ; how to fill in an entry
        sub.l   a2,a2                    ; start at beginning of Thing list
        jsr     gu_mklis                 ; make a list
gtl_exit
        movem.l (sp)+,gtl.reg
        rts
;+++
; Fill in next Thing.  Called from GU_MKLIS at the request of GU_MKTHL etc.
;
;       Registers:
;               Entry                           Exit
;       D0                                      error code
;       D1      Owner or -1                     preserved
;       D2      Thing select                    preserved
;       A1      entry to fill in                preserved
;       A2      pointer to Thing name           updated
;       A3      unique character list           updated
;---
gtl_info
gti.reg reg     d1/d2/d3/d4/a0/a1/a4
stk_user equ    $00
stk_type equ    $04

        movem.l gti.reg,-(sp)
        move.l  a1,a4                   ; keep entry base
gti_next
        move.l  a2,a0                   ; previous thing
        moveq   #sms.nthg,d0
        jsr     gu_thjmp                ; get next
        bne.l   gti_eof                 ; ... oops
        move.l  a1,d0                   ; any more
        beq.s   gti_eof
        move.l  th_thing(a1),a0         ; thing itself
        move.l  thh_type(a0),d1         ; type
        moveq   #-1,d0
        cmp.l   d0,d1                   ; THING's own linkage?
        beq.s   gti_eof                 ; ... yes, end
        lea     th_name(a1),a2          ; ... no, set next
        move.l  stk_type(sp),d2         ; just exec?
        beq.s   gti_set                 ; ... no
        cmp.l   d2,d1                   ; exec?
        bne.s   gti_next

gti_set
        move.l  a1,thl_thgl(a4)         ; set thing linkage pointer
        move.l  a0,thl_thg(a4)          ; thing pointer
        move.l  d1,thl_type(a4)         ; and type

        move.l  a2,a0                   ; (nthu requires this)
        move.l  stk_user(sp),d4         ; specific user?
        tst.w   d4
        blt.s   gti_sname               ; ... no
        sub.l   a1,a1                   ; start at first user
        moveq   #-1,d2                  ; preset no job
gti_usloop
        moveq   #sms.nthu,d0
        jsr     gu_thjmp
        move.l  a0,a2                   ; reset next
        bne.s   gti_next                ; nothing found
        cmp.l   d4,d2                   ; ours?
        beq.s   gti_sname
        move.l  a1,d0
        beq.s   gti_next                ; all found
        bne.s   gti_usloop

gti_sname
        move.w  (a0)+,d3                ; get actual length
        move.l  a3,d2                   ; unique character required?
        beq.s   gti_ckln                ; ... no
        addq.w  #2,d3                   ; name is two longer

gti_ckln
        moveq   #thl.maxn,d0            ; name can be this long
        cmp.w   d0,d3                   ; too long?
        ble.s   gti_cpch                ; no
        move.w  d0,d3                   ; yes, use max length
gti_cpch
        lea     thl_name(a4),a1         ; point to name in entry
        move.w  d3,(a1)+                ; keep length
        tst.l   d2
        beq.s   gti_cpce                ; no additional
        moveq   #k.space,d2
        move.b  (a3)+,(a1)              ; unique character
        cmp.b   (a1)+,d2                ; space?
        bne.s   gti_spce                ; ... no
        subq.l  #1,a3                   ; ... yes, end of character list
gti_spce
        move.b  d2,(a1)+
        subq.w  #2,d3                   ; do not need to copy this much
        bra.s   gti_cpce
gti_cpcl
        move.b  (a0)+,(a1)+             ; copy characters of name
gti_cpce
        dbra    d3,gti_cpcl
*
        moveq   #0,d0                   ; no errors
gti_exit
        movem.l (sp)+,gti.reg
        rts
gti_eof
        moveq   #err.eof,d0
        bra.s   gti_exit
        end
