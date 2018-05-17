; Standard Procedure / Function entry    V2.02    Tony Tebby   QJUMP

        section utils

        xdef    ut_proc
        xdef    ut_fun40
        xdef    ut_fun

        xref    ut_chan1
        xref    ut_chkri
        xref    ut_gtli1
        xref    ut_gtin1
        xref    ut_gtnm1
        xref    ut_retst
        xref    ut_retin
        xref    ut_retfp
        xref    ut_lfloat
        xref    gu_thjmp
        xref    gu_achp0
        xref    gu_rchp

        include 'dev8_keys_err'
        include 'dev8_keys_thg'
        include 'dev8_keys_qdos_sms'
        include 'dev8_keys_sbasic'

;+++
; This routine handles the entry into a standard thing function.
; It ensures that there is at least 40 bytes spare for the return argument.
;
; There should be two pointers on the stack on entry. The first is
; the pointer to the THING name, the second is the pointer to the EXTENSION
; name
;---
ut_fun40
        moveq   #40,d7                   ; check for 40 bytes
;+++
; This routine handles the entry into a standard thing function.
;
; There should be two pointers on the stack on entry. The first is
; the pointer to the THING name, the second is the pointer to the EXTENSION
; name
;
;       d7 c s  (long) space required for return argument
;---
ut_fun
        move.l  d7,d1
        jsr     ut_chkri                 ; check RI stack

        move.l  (sp)+,a0                 ; thing
        move.l  (sp)+,a1                 ; extension

        bsr.l   ut_pfun                  ; do extension
        blt.s   ut_pexit                 ; ... oops

        move.l  d6,a0                    ; return parameter pointer
        beq.s   utf_rtprm
        clr.l   (a0)                     ; null return

utf_rtprm
        move.l  sb_arthp(a6),a1            ; ... cleaned up stack
        lsl.w   #16-thp..str,d5          ; string?
        bcs.s   utf_str                  ; ... yes
        lsl.w   #thp..str-thp..lng,d5    ; long word?
        bcs.s   utf_long                 ; ... yes
        lsl.w   #thp..lng-thp..wrd,d5    ; word?
        bcs.s   utf_word                 ; ... yes
        lsl.w   #thp..wrd-thp..byt,d5    ; byte?
        bcs.s   utf_byte                 ; ... yes
        lsl.w   #thp..byt-thp..chr,d5    ; character?
        bcs.s   utf_char                 ; ... no

utf_nimp
        moveq   #err.nimp,d0
        bra.s   ut_pexit
utf_byte
        move.b  (a0),-1(a6,a1.l)         ; set byte
        clr.b   -2(a6,a1.l)
        bra.s   utf_rtwrd
utf_word
        move.w  (a0),-2(a6,a1.l)         ; set word
utf_rtwrd
        subq.w  #2,a1
        jsr     ut_retin                 ; return integer type
        bra.s   ut_pexit

utf_long
        move.l  (a0),d1                  ; convert to float
        jsr     ut_lfloat
        jsr     ut_retfp                 ; return float
        bra.s   ut_pexit

utf_char
        move.w  (a0),-2(a6,a1.l)
        move.w  #1,-4(a6,a1.l)           ; one byte string
        bra.s   utf_rtst

utf_str
        moveq   #3,d0
        add.w   (a0),d0                  ; round up length of string
        bclr    #0,d0
        add.w   d0,a0
utf_cpys
        subq.l  #2,a1                    ; copy string onto stack
        move.w  -(a0),(a6,a1.l)
        subq.w  #2,d0
        bgt.s   utf_cpys

utf_rtst
        jsr     ut_retst                 ; return string
        bra.s   ut_pexit
;+++
; This routine handles the entry into a standard thing procedure.
;
; There should be two pointers on the stack on entry. The first is
; the pointer to the THING name, the second is the pointer to the EXTENSION
; name.
;---
ut_proc
        moveq   #0,d7                    ; amount spare
        move.l  (sp)+,a0                 ; thing
        move.l  (sp)+,a1                 ; extension
        bsr.s   ut_pfun
        ble.s   ut_pexit                 ; ok or error
        moveq   #0,d0
ut_pexit
        move.l  a5,d1                    ; any memory to return?
        beq.s   ut_pdone
        move.l  a5,a0
        jsr     gu_rchp                  ; return heap
ut_pdone
        tst.l   d0
        rts

ut_pfun
        move.l  sb_arthb(a6),d6          ; extent of RI stack
        sub.l   sb_arthp(a6),d6            ;
        moveq   #sms.uthg,d0             ; use thing
        moveq   #sms.myjb,d1             ; for my job
        move.l  (a1),d2                  ; extension name
        moveq   #127,d3                  ; wait for 2.5 seconds
        jsr     gu_thjmp
        bne.l   utp_nexit                ; ... oops

        move.l  a1,a0                    ; keep thing address

        move.l  thh_pdef(a1),a4
        add.l   a1,a4                    ; call parameter defs

        move.l  sb_buffb(a6),d4          ; temporary parameter list

utp_call
        move.w  (a4)+,d5                 ; next parameter type
        beq.l   utp_sprm
        tst.l   d7                       ; function?
        beq.s   utp_cchan                ; ... no
        tst.w   (a4)                     ; last parameter?
        beq.l   utp_sret                 ; yes, set return type

utp_cchan
        cmp.w   #thp.chid,d5             ; channel ID?
        bne.s   utp_ctyp

        moveq   #4,d1
        jsr     ut_chkri

        moveq   #1,d1
        cmp.l   a3,a5                    ; are there any parameters?
        ble.s   utp_chan
        btst    #7,1(a6,a3.l)            ; has the first parameter a hash?
        beq.s   utp_chan                 ; ... no

        bsr.l   ut_gtin1                 ; get one integer
        bne.l   utp_nmem                 ; oops
        move.w  0(a6,a1.l),d1            ; get value to replace the default
        addq.l  #2,a1                    ; reset ri stack pointer
        blt.l   utp_ipar
        addq.l  #8,a3                    ; we've taken this param

utp_chan
        mulu    #$28,d1                  ; make d1 pointer to channel table
        add.l   sb_chanb(a6),d1
        cmp.l   sb_chanp(a6),d1          ; is it within the table?
        bge.l   utp_ichn                 ; ... no
        move.l  0(a6,d1.l),d1            ; get channel id
        tst.w   d1                       ; is it open?
        bmi.l   utp_ichn                 ; ... no

        moveq   #thp..cid,d0
        subq.l  #4,a1
        move.l  d1,(a6,a1.l)             ; set channel ID on stack
        subq.l  #8,a3                    ; parameter pointer will be skipped
        bra.l   utp_sett

utp_ctyp
        cmp.l   a5,a3                    ; any parameters left?
        bge.l   utp_null
        move.w  #$0f0f,d2
        and.w   (a6,a3.l),d2             ; parameter type
        beq.l   utp_null

        move.w  d5,d0                    ; check for type
        lsl.w   #16-thp..str,d0          ; string?
        bcc.s   utp_ckval                ; ... no
        moveq   #thp.ulng+thp.uwrd+thp.ubyt,d1
        and.w   d5,d1                    ; numeric bits
        beq.l   utp_str                  ; ... none
        subq.b  #1,d2                    ; value type?
        ble.l   utp_str                  ; ... no
        tst.b   (a6,a3.l)                ; set?
        ble.l   utp_str                  ; ... no
utp_ckval
        lsl.w   #thp..str-thp..lng,d0    ; long word?
        bcs.l   utp_long                 ; ... yes
        lsl.w   #thp..lng-thp..wrd,d0    ; word?
        bcs.s   utp_word                 ; ... yes
        lsl.w   #thp..wrd-thp..byt,d0    ; byte?
        bcs.s   utp_byte                 ; ... yes
        lsl.w   #thp..byt-thp..chr,d0    ; character?
        bcs.s   utp_char                 ; ... yes
        bra.l   utp_nimp                 ; ... no

utp_char
        jsr     ut_gtnm1                 ; get character
        bne.l   utp_nmem
        sub.w   #1,(a6,a1.l)             ; one byte?
        bne.l   utp_ipar                 ; ... no
        move.b  2(a6,a1.l),3(a6,a1.l)    ; ... set character
        clr.b   2(a6,a1.l)
        moveq   #thp..chr,d0
        bra.s   utp_sett

utp_byte
        jsr     ut_gtli1                 ; get a long integer
        bne.l   utp_nmem
        moveq   #thp..byt,d0             ; type is byte
        move.l  (a6,a1.l),d1
        btst    #thp..sgn,d5             ; unsigned?
        beq.s   utp_ubyt
        ext.w   d1                       ; signed
        ext.l   d1
        cmp.l   (a6,a1.l),d1             ; is extended the same?
        bne.l   utp_orng                 ; ... no
        bra.s   utp_sett
utp_ubyt
        clr.b   d1                       ; clear byte
        tst.l   d1                       ; should be zero
        bne.l   utp_orng                 ; ... not zero
        bra.s   utp_sett

utp_word
        jsr     ut_gtli1                 ; get an integer
        bne.l   utp_nmem
        moveq   #thp..wrd,d0             ; type is word
        move.l  (a6,a1.l),d1
        btst    #thp..sgn,d5             ; unsigned?
        beq.s   utp_uwrd
        ext.l   d1
        cmp.l   (a6,a1.l),d1             ; is extended the same?
        bne.l   utp_orng                 ; ... no
        bra.s   utp_sett
utp_uwrd
        clr.w   d1                       ; clear word
        tst.l   d1                       ; should be zero
        bne.l   utp_orng                 ; ... not zero
        bra.s   utp_sett

utp_long
        jsr     ut_gtli1                 ; get long integer
        bne.l   utp_nmem
        moveq   #thp..lng,d0
        bra.s   utp_sett

utp_str
        jsr     ut_gtnm1                 ; get name
        bne.l   utp_nmem
        moveq   #thp..str,d0
        bra.s   utp_sett

utp_null
        moveq   #0,d0                    ; null parameter
        btst    #thp..opt,d5             ; null parameter allowed?
        beq.l   utp_ipar                 ; ... bad parameter
        move.l  sb_arthp(a6),a1          ; where it is

utp_sett
        move.l  a1,sb_arthp(a6)          ; current RI stack (name strips it!!)
        move.w  d5,(a6,d4.l)             ; parameter required
        move.w  d0,2(a6,d4.l)            ; what we have
        move.l  a1,d0
        sub.l   sb_arthb(a6),d0
        move.l  d0,4(a6,d4.l)            ; where it is
        addq.l  #8,d4                    ; one more definition
        addq.l  #8,a3                    ; go on one parameter
        bra.l   utp_call

utp_sret        ; set return parameters
        clr.w   (a6,d4.l)                ; end of call list
        addq.l  #8,d4                    ; ... but space for return def

utp_sprm
        cmp.l   a5,a3                    ; any parameters left
        blt.l   utp_ipar                 ; ...yes

        move.l  a0,a4                    ; address of thing
        sub.l   a5,a5                    ; assume no memory used
        clr.w   (a6,d4.l)                ; mark end of temporary parameter list
        move.l  sb_arthb(a6),d0
        sub.l   sb_arthp(a6),d0          ; size of RI stack
        sub.l   d6,d0                    ; increase in size
        add.l   d0,sb_arthp(a6)          ; restore stack
        add.l   d7,d0                    ; + return argument size
        sub.l   sb_buffb(a6),d4          ; parameter list size
        add.l   d4,d0
        beq.l   utp_dothg
        jsr     gu_achp0                 ; allocate a bit of heap
        bne.l   utp_nmem
        move.l  a0,a5                    ; keep base of area safe
        lea     (a0,d4.l),a1             ; start of parameters
        move.l  sb_buffb(a6),d4          ; start of temporary parameter list

utp_sploop
        move.w  (a6,d4.l),d1             ; value, pointer or end
        beq.s   utp_doret                ; ... end
        blt.s   utp_sptr                 ; ... pointer
        tst.w   2(a6,d4.l)               ; ... value, is it null?
        beq.s   utp_nulv                 ; ... yes
        move.l  4(a6,d4.l),d0
        add.l   sb_arthb(a6),d0
        move.l  (a6,d0.l),(a0)+          ; set value
        bra.s   utp_spend
utp_nulv
        btst    #thp..nnl,d1             ; null negative?
        beq.s   utp_nulz
        subq.l  #1,(a0)
utp_nulz
        addq.l  #4,a0                    ; null value
        bra.s   utp_spend

utp_sptr
        move.w  2(a6,d4.l),d2            ; type of parameter
        beq.s   utp_nulp                 ; ... null
        move.l  4(a6,d4.l),d0            ; pointer to parameter
        add.l   sb_arthb(a6),d0
        move.w  #thp.call,d3
        bset    d2,d3                    ; formal type
        move.l  a1,d1                    ; pointer
        subq.w  #thp..str,d2             ; string?
        bne.s   utp_pval                 ; ... no
        move.w  (a6,d0.l),d2             ; ... yes, length
        addq.w  #1,d2
utp_pstr
        move.w  (a6,d0.l),(a1)+          ; copy (word aligned)
        addq.l  #2,d0
        subq.w  #2,d2
        bge.s   utp_pstr
        bra.s   utp_spprm
utp_pval
        addq.w  #thp..str-thp..lng,d2    ; long word?
        beq.s   utp_cpprm                ; ... yes
        addq.w  #2,d1                    ; ... no, point to word
        addq.w  #thp..lng-thp..wrd,d2    ; word?
        beq.s   utp_cpprm                ; ... yes
        addq.w  #1,d1                    ; ... no, point to byte/char
utp_cpprm
        move.l  (a6,d0.l),(a1)+
utp_spprm
        move.w  d3,(a0)+                 ; type
        clr.w   (a0)+                    ; extra
        move.l  d1,(a0)+                 ; pointer
        bra.s   utp_spend

utp_nulp
        clr.l   (a0)+                    ; null pointer
        clr.l   (a0)+

utp_spend
        addq.l  #8,d4
        bra.s   utp_sploop

utp_doret
        tst.l   d7                       ; any return?
        beq.s   utp_doprm                ; ... no
        move.w  d5,(a0)+                 ; return parameter
        clr.w   (a0)+
        move.l  a1,(a0)
        move.l  a1,d6                    ; keep pointer
        btst    #thp..str,d5             ; string?
        beq.s   utp_doprm                ; ... no
        move.w  d7,-(a0)                 ; ... yes, set max length
        subq.w  #2,(a0)

utp_doprm
        move.l  a5,a1                    ; set parameter pointer
utp_dothg
        jsr     thh_code(a4)             ; do the procedure
        bra.s   utp_fthg

utp_ichn
        moveq   #err.ichn,d0
        bra.s   utp_nmem
utp_nimp
        moveq   #err.nimp,d0
        bra.s   utp_nmem
utp_orng
        moveq   #err.orng,d0
        bra.s   utp_nmem
utp_ipar
        moveq   #err.ipar,d0
utp_nmem
        sub.l   a5,a5                    ; no memory allocated

utp_fthg
        lea     th_name(a2),a0           ; thing name
        move.l  d0,d4                    ; save error code
        moveq   #sms.fthg,d0             ; free thing
        moveq   #sms.myjb,d1             ; ... mine
        jsr     gu_thjmp
        move.l  d4,d0
        rts
utp_nexit
        sub.l   a5,a5                    ; no heap allocated
        tst.l   d0
        rts
        end
