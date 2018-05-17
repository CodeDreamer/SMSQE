; SMSQ_LANG  Language Dependent Modules  V2.0   1994  Tony Tebby

        xref    smsq_pref
        xref    smsq_end

        include 'dev8_keys_qdos_sms'

        section header

header_base
        dc.l    msg_base-header_base     ; length of header
        dc.l    0                        ; module length unknown
        dc.l    smsq_end-msg_base        ; loaded length
        dc.l    0                        ; checksum
        dc.l    0                        ; always select
        dc.b    0                        ; main level
        dc.b    0
        dc.w    smsq_name-*

smsq_name
        dc.w    14,'SMSQ Messages '
        dc.l    '    '
        dc.w    $200a


        section base
msg_base
        lea     smsq_pref,a1             ; link in language dependent modules
        moveq   #sms.lldm,d0             ; starting with preferences
        trap    #do.sms2
        rts
        end
