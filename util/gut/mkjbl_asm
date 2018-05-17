; Make a list of Jobs             V0.04   1988  J.R.Oakley  QJUMP
;                                                   1989  Tony Tebby
        section gen_util
*
        include 'dev8_keys_chn'
        include 'dev8_keys_err'
        include 'dev8_keys_k'
        include 'dev8_keys_sys'
        include 'dev8_keys_con'
        include 'dev8_keys_qdos_io'
        include 'dev8_keys_qdos_sms'
        include 'dev8_keys_jbl'
        include 'dev8_mac_assert'
*
        xref    gu_mklis
        xref    gu_iow
        xref    met_anon
        xref    met_sbas
*
        xdef    gu_mkjbl
        xdef    gu_mkpwl
;+++
; Make a list of all jobs.
; If required a unique letter (and space) will be added to the
; start of each jobname. This is defined by a list of bytes ending with a
; space.
;
;       Registers:
;               Entry                           Exit
;       D0                                      0 or error code
;       D1                                      number of entries
;       A1                                      pointer to list
;       A3      0 or unique character list      updated
*---
gu_mkjbl
gjl.reg reg    d2/a0/a2
        movem.l gjl.reg,-(sp)
        move.w  #-1,a2                   ; no channel, do not fill in primary
        bra.s   gjl_do
;+++
; Make a list of all jobs and fill in the primary window ID.
; An extop is used to run down the list, so a primary channel ID which is
; known to be free must be supplied - this will usually be owned by the
; calling job. If required a unique letter (and space) will be added to the
; start of each jobname. This is defined by a list of bytes ending with a
; space.
;
;       Registers:
;               Entry                           Exit
;       D0                                      0 or error code
;       D1                                      number of entries
;       A0      primary window channel ID       preserved
;       A1                                      pointer to list
;       A3      0 or unique character list      updated
;---
gu_mkpwl
        movem.l gjl.reg,-(sp)
        move.l  a0,a2                   ; keep channel safe
gjl_do
        moveq   #0,d1                   ; start with Job 0
        moveq   #jbl.elen,d0            ; entry length
        lea     gjl_info(pc),a0         ; how to fill in an entry
        jsr     gu_mklis(pc)            ; make a list
        movem.l  (sp)+,gjl.reg
        rts
*+++
; Fill in next job.  Called from GU_MKLIS at the request of GU_MKJBL and
; GU_MKPWL.
;
;       Registers:
;               Entry                           Exit
;       D0                                      error code
;       D1      job to test or -1 (end)         next job to test
;       D2
;       A1      entry to fill in                preserved
;       A2      channel ID / -1                 preserved
;       A3      pointer to unique character     updated
*---
gjl_info
gjli.reg reg     d3/d4/d5/d6/a0/a1/a2/a4/a5
gjl_chn equ     $18
        movem.l gjli.reg,-(sp)
        move.l  a1,a4                   ; keep entry base
gjl_nxjb
        tst.w   d1                      ; was the last one the last one? (!)
        bmi.l   gjl_eof                 ; yes
        move.l  d1,d4
*
gjl_retry
        move.l  d1,jbl_id(a4)           ; no, fill in its Job ID
        moveq   #0,d2                   ; with job 0 at top of tree
        moveq   #sms.injb,d0            ; get some info on it
        trap    #do.sms2
        tst.l   d0                      ; did it exist?
        beq.s   gjl_next
        tst.l   jbl_id(a4)              ; did we need one?
        beq.s   gjl_retry               ; no, not really
        bra.l   gjl_exit                ; yes, whoops
gjl_next
        move.l  d1,d6                   ; is there a next job?
        bne.s   gjl_setv                ; yes
        moveq   #-1,d6                  ; no
*
gjl_setv
        move.l  a0,jbl_base(a4)         ; keep some of the info
        move.l  d2,jbl_ownr(a4)
        assert  jbl_susp+2,jbl_prty
        move.l  d3,jbl_susp(a4)
        lea     6(a0),a5                ; keep pointer to name flag
        moveq   #0,d5                   ; depth
        bra.s   gjl_leve
gjl_levl
        addq.w  #1,d5                   ; one (more) level down
        moveq   #0,d2
        moveq   #sms.injb,d0
        trap    #do.sms2
        tst.l   d0
        bne.s   gjl_levs
gjl_leve
        move.l  d2,d1                   ; look at owner
        bne.s   gjl_levl
gjl_levs
        move.w  d5,jbl_levl(a4)         ; set level

        moveq   #-1,d1                  ; preset no primary

        move.l  gjl_chn(sp),a0          ; get channel ID
        move.w  a0,d0                   ; any?
        blt.s   gjl_setch               ; ... no channel

        moveq   #0,d2                   ; no bytes
        moveq   #iop.slnk,d0            ; to set in linkage block
        jsr     gu_iow                  ; we want its base
*
        move.l  d4,d2                   ; this job
        moveq   #iow.xtop,d0            ; by using an EXTOP
        lea     gjl_xtop(pc),a2
        jsr     gu_iow

gjl_setch
        move.l  d1,jbl_prch(a4)

        cmp.w   #$4afb,(a5)+            ; name flag? $$$$
        beq.s   gjl_cpnm                ; yes, copy it
        lea     met_anon(pc),a5         ; no, call it '*** Anon ***'
        tst.l   d4                      ; but is it SuperBASIC?
        bne.s   gjl_cpnm                ; no
        lea     met_sbas(pc),a5         ; yes, call it that
gjl_cpnm
        move.w  (a5)+,d3                ; get actual length
        move.l  a3,d2                   ; unique character required?
        beq.s   gjl_cklv                ; ... no
        addq.w  #2,d3                   ; name is two longer

gjl_cklv
        add.w   d5,d5                   ; extra length
        moveq   #jbl.maxn-10,d0         ; do not take too mauch room
        cmp.w   d0,d5
        ble.s   gjl_stlv
        move.l  d0,d5
gjl_stlv
        add.w   d5,d3                   ; new total length

        moveq   #jbl.maxn,d0            ; name can be this long
        cmp.w   d0,d3                   ; too long?
        ble.s   gjl_cpch                ; no
        move.w  d0,d3                   ; yes, use max length
gjl_cpch
        lea     jbl_name(a4),a0         ; point to name in entry
        move.w  d3,(a0)+                ; keep length
        tst.l   d2
        beq.s   gjl_cpce                ; no additional
        moveq   #k.space,d2
        move.b  (a3)+,(a0)              ; unique character
        cmp.b   (a0)+,d2                ; space?
        bne.s   gjl_spce                ; ... no
        subq.l  #1,a3                   ; ... yes, end of character list
gjl_spce
        move.b  d2,(a0)+
        subq.w  #2,d3                   ; do not need to copy this much
        sub.w   d5,d3

        bra.s   gjl_lvle
gjl_lvlp
        move.w  #'> ',(a0)+             ; copy level indicators
gjl_lvle
        subq.w  #2,d5
        bge.s   gjl_lvlp

        bra.s   gjl_cpce
gjl_cpcl
        move.b  (a5)+,(a0)+             ; copy characters of name
gjl_cpce
        dbra    d3,gjl_cpcl
*
        move.l  d6,d1                   ; next job
        moveq   #0,d0                   ; no errors
gjl_exit
        movem.l (sp)+,gjli.reg
        rts
gjl_eof
        moveq   #err.eof,d0
        bra.s   gjl_exit
*
gjl_xtop
        lea     pt_head(a1),a1          ; look through primaries from top
gjx_loop
        move.l  (a1),d3                 ; get next primary
        beq.s   gjx_nf                  ; none, job has no primary
        move.l  d3,a1
        cmp.l   chn_ownr-sd_prwlt-sd.extnl(a1),d2 ; is primary owned by job?
        bne.s   gjx_loop                 ; no, try next

        lea     -sd_prwlt-sd.extnl(a1),a1 ; base of channel
        move.l  chn_tag(a1),d1
        clr.w   d1

        move.l  sys_chtb(a6),a2          ; channel table
gjx_chlook
        cmp.l   (a2)+,a1                 ; our channel?
        beq.s   gjx_ok                   ; ... yes
        addq.w  #1,d1                    ; next number
        cmp.w   sys_chtp(a6),d1
        ble.s   gjx_chlook
gjx_nf
        moveq   #-1,d1                   ; ... no ID
gjx_ok
        moveq   #0,d0                    ; OK
gjx_exit
        rts

*
        end
