; Find Thing, internal routine          V0.01   Feb 1988  J.R.Oakley  QJUMP;

        section thing

        xdef    th_find

        xref    th_chekb
        xref    cv_streq

        include 'dev8_mac_assert'
        include 'dev8_keys_err'
        include 'dev8_keys_sys'
        include 'dev8_keys_thg'

;+++
; This routine is used internally to find a thing, if it exists: if we want
; to add a new thing then we don't want it to exist, if we want to use one
; then we do.
;
;       Registers:
;               Entry                           Exit
;       D0                                      0 or ERR.ITNF
;       D7      msb set for fixed version
;       A0      name of thing (>=1 chars)       preserved
;       A1                                      pointer to linkage block
;       A6      pointer to system variables     preserved
;       status return standard
;---
th_find
thf.reg reg     d1-d2/a2
        movem.l thf.reg,-(sp)

        jsr     th_chekb                 ; get check byte
        move.b  d0,d1                    ; keep it
        assert  th_nxtth,0
        lea     sys_thgl(a6),a2          ; point to thing list
        tst.l   d7                       ; fixed?
        bpl.s   thf_norm                 ; ... no, normal

thf_oloop
        move.l  th_nxtth(a2),d2
        beq.s   thf_exnf                 ; not found

        move.l  d2,a2                    ; point to linkage block
        assert  th_nxtth,0
        lea     th_name(a2),a1           ; point to this thing's name
        jsr     cv_streq                 ; check for equality
        bne.s   thf_oloop                ; ... no
        bra.s   thf_found

thf_norm
thf_loop
        move.l  th_nxtth(a2),d2
        beq.s   thf_exnf                 ; not found

        move.l  d2,a2                    ; point to linkage block
        assert  th_nxtth,0
        cmp.b   th_check(a2),d1          ; same checkbyte?
        bne.s   thf_loop                 ; ... no

        lea     th_name(a2),a1           ; point to this thing's name
        jsr     cv_streq                 ; check for equality
        bne.s   thf_loop                 ; ... no

thf_found
        move.l  a2,a1                    ; set return parameter

thf_exit
        movem.l (sp)+,thf.reg
        rts
thf_exnf
        moveq   #err.itnf,d0
        bra.s   thf_exit
        end
